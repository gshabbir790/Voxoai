package com.example.data.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import android.util.Base64
import com.example.data.model.MusicTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.*

class AudioEngine(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var isPrepared = false
    private var currentPlayingPath: String? = null

    // Callbacks
    var onCompletionListener: (() -> Unit)? = null
    var onErrorListener: ((String) -> Unit)? = null

    /**
     * Wrap raw 16-bit PCM bytes into standard RIFF WAV container.
     */
    fun pcmToWav(pcmData: ByteArray, sampleRate: Int = 24000, channels: Int = 1): ByteArray {
        val header = createWavHeader(pcmData.size, sampleRate, channels, 16)
        val out = ByteArrayOutputStream(header.size + pcmData.size)
        out.write(header)
        out.write(pcmData)
        return out.toByteArray()
    }

    private fun createWavHeader(pcmLength: Int, sampleRate: Int, channels: Int, bitsPerSample: Int): ByteArray {
        val totalDataLen = pcmLength + 36
        val byteRate = sampleRate * channels * bitsPerSample / 8
        val blockAlign = channels * bitsPerSample / 8

        val header = ByteArray(44)
        // RIFF chunk descriptor
        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = (totalDataLen shr 8 and 0xff).toByte()
        header[6] = (totalDataLen shr 16 and 0xff).toByte()
        header[7] = (totalDataLen shr 24 and 0xff).toByte()
        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()
        // 'fmt ' sub-chunk
        header[12] = 'f'.code.toByte()
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()
        header[16] = 16 // 16 for PCM format chunk size
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1 // Audio format 1 = PCM
        header[21] = 0
        header[22] = channels.toByte()
        header[23] = 0
        header[24] = (sampleRate and 0xff).toByte()
        header[25] = (sampleRate shr 8 and 0xff).toByte()
        header[26] = (sampleRate shr 16 and 0xff).toByte()
        header[27] = (sampleRate shr 24 and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = (byteRate shr 8 and 0xff).toByte()
        header[30] = (byteRate shr 16 and 0xff).toByte()
        header[31] = (byteRate shr 24 and 0xff).toByte()
        header[32] = blockAlign.toByte()
        header[33] = 0
        header[34] = bitsPerSample.toByte()
        header[35] = 0
        // 'data' sub-chunk
        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()
        header[40] = (pcmLength and 0xff).toByte()
        header[41] = (pcmLength shr 8 and 0xff).toByte()
        header[42] = (pcmLength shr 16 and 0xff).toByte()
        header[43] = (pcmLength shr 24 and 0xff).toByte()

        return header
    }

    /**
     * Decode Base64 data from Gemini TTS into WAV file.
     */
    suspend fun saveBase64AudioToFile(base64Data: String, mimeType: String, outputFileName: String): File = withContext(Dispatchers.IO) {
        val audioBytes = Base64.decode(base64Data, Base64.DEFAULT)
        val audioDir = File(context.filesDir, "voxora_audio").apply { mkdirs() }
        val targetFile = File(audioDir, outputFileName)

        val finalWavBytes = if (mimeType.contains("pcm", ignoreCase = true) || !isWavFormat(audioBytes)) {
            pcmToWav(audioBytes, sampleRate = 24000, channels = 1)
        } else {
            audioBytes
        }

        FileOutputStream(targetFile).use { fos ->
            fos.write(finalWavBytes)
        }
        targetFile
    }

    private fun isWavFormat(bytes: ByteArray): Boolean {
        if (bytes.size < 12) return false
        return bytes[0] == 'R'.code.toByte() &&
               bytes[1] == 'I'.code.toByte() &&
               bytes[2] == 'F'.code.toByte() &&
               bytes[3] == 'F'.code.toByte() &&
               bytes[8] == 'W'.code.toByte() &&
               bytes[9] == 'A'.code.toByte() &&
               bytes[10] == 'V'.code.toByte() &&
               bytes[11] == 'E'.code.toByte()
    }

    /**
     * Extract raw 16-bit PCM samples from a WAV file.
     */
    private fun extractPcmFromWav(wavBytes: ByteArray): ByteArray {
        if (!isWavFormat(wavBytes) || wavBytes.size <= 44) return wavBytes
        // Look for "data" chunk
        var offset = 12
        while (offset + 8 <= wavBytes.size) {
            val chunkId = String(wavBytes, offset, 4)
            val chunkSize = ByteBuffer.wrap(wavBytes, offset + 4, 4).order(ByteOrder.LITTLE_ENDIAN).int
            if (chunkId == "data") {
                val dataStart = offset + 8
                val dataEnd = min(wavBytes.size, dataStart + max(0, chunkSize))
                return wavBytes.copyOfRange(dataStart, dataEnd)
            }
            offset += 8 + chunkSize
        }
        return wavBytes.copyOfRange(44, wavBytes.size)
    }

    /**
     * Synthesize rich procedural background music loop corresponding to the MusicTrack.
     */
    private fun synthesizeMusicTrack(track: MusicTrack, totalSamples: Int, sampleRate: Int = 24000): ShortArray {
        val musicSamples = ShortArray(totalSamples)
        if (track.id == "none" || track.baseFrequencyHz <= 0f) return musicSamples

        val chords = track.chordProgression
        val numChords = chords.size
        val samplesPerChord = (sampleRate * 2.0).toInt().coerceAtLeast(sampleRate)

        for (i in 0 until totalSamples) {
            val t = i.toDouble() / sampleRate
            val chordIndex = ((i / samplesPerChord) % numChords)
            val baseFreq = chords[chordIndex].toDouble()

            // Layered harmonic oscillator (warm pad/acoustic texture)
            val wave1 = sin(2.0 * PI * baseFreq * t)
            val wave2 = 0.5 * sin(2.0 * PI * (baseFreq * 1.5) * t) // Fifth
            val wave3 = 0.3 * sin(2.0 * PI * (baseFreq * 2.0) * t) // Octave
            val waveSub = 0.4 * sin(2.0 * PI * (baseFreq * 0.5) * t) // Sub bass

            // Subtle slow tremolo / modulation
            val lfo = 0.8 + 0.2 * sin(2.0 * PI * 0.5 * t)
            val combined = (wave1 + wave2 + wave3 + waveSub) * 0.25 * lfo

            val sampleVal = (combined * Short.MAX_VALUE * 0.4).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
            musicSamples[i] = sampleVal.toShort()
        }
        return musicSamples
    }

    /**
     * Mix voice audio with background music and intelligent Auto Ducking.
     */
    suspend fun mixVoiceAndMusic(
        voiceWavFile: File,
        musicTrack: MusicTrack,
        voiceVolume: Float = 1.0f,
        musicVolume: Float = 0.35f,
        autoDucking: Boolean = true,
        duckingAmountPercent: Int = 75,
        duckingAttackMs: Int = 100,
        duckingReleaseMs: Int = 400,
        outputFileName: String
    ): File = withContext(Dispatchers.IO) {
        val voiceWavBytes = voiceWavFile.readBytes()
        val voicePcm = extractPcmFromWav(voiceWavBytes)
        val numVoiceSamples = voicePcm.size / 2

        if (numVoiceSamples == 0 || musicTrack.id == "none") {
            val audioDir = File(context.filesDir, "voxora_audio").apply { mkdirs() }
            val directOutput = File(audioDir, outputFileName)
            voiceWavFile.copyTo(directOutput, overwrite = true)
            return@withContext directOutput
        }

        val sampleRate = 24000
        val voiceBuffer = ByteBuffer.wrap(voicePcm).order(ByteOrder.LITTLE_ENDIAN)
        val voiceSamples = ShortArray(numVoiceSamples) { voiceBuffer.short }

        // Extra 1.5 seconds tail for graceful music fade out
        val totalMixSamples = numVoiceSamples + (sampleRate * 1.5).toInt()
        val musicSamples = synthesizeMusicTrack(musicTrack, totalMixSamples, sampleRate)

        // Envelope detection for speech amplitude (20ms window)
        val windowSize = sampleRate / 50 // 20ms = 480 samples
        val duckMultiplier = if (autoDucking) (100 - duckingAmountPercent).toFloat() / 100f else 1.0f

        var currentDuckingGain = 1.0f
        val attackStep = 1.0f / (duckingAttackMs * sampleRate / 1000f).coerceAtLeast(1f)
        val releaseStep = 1.0f / (duckingReleaseMs * sampleRate / 1000f).coerceAtLeast(1f)

        val mixedSamples = ShortArray(totalMixSamples)

        for (i in 0 until totalMixSamples) {
            val voiceSample = if (i < numVoiceSamples) voiceSamples[i].toFloat() * voiceVolume else 0f

            // Determine if voice is active in current window
            val isSpeaking = if (i < numVoiceSamples) {
                val start = (i - windowSize / 2).coerceAtLeast(0)
                val end = (i + windowSize / 2).coerceAtMost(numVoiceSamples - 1)
                var sumSquares = 0.0
                for (j in start..end) {
                    val s = voiceSamples[j].toDouble()
                    sumSquares += s * s
                }
                val rms = sqrt(sumSquares / (end - start + 1))
                rms > 600.0 // speech activity threshold
            } else {
                false
            }

            // Smoothly adjust ducking gain
            if (isSpeaking) {
                currentDuckingGain = (currentDuckingGain - attackStep).coerceAtLeast(duckMultiplier)
            } else {
                currentDuckingGain = (currentDuckingGain + releaseStep).coerceAtMost(1.0f)
            }

            // Music fade out at the very end
            val endFade = if (i >= numVoiceSamples) {
                val remaining = totalMixSamples - i
                (remaining.toFloat() / (totalMixSamples - numVoiceSamples)).coerceIn(0f, 1f)
            } else 1.0f

            val musicSample = musicSamples[i].toFloat() * musicVolume * currentDuckingGain * endFade
            val sum = voiceSample + musicSample

            // Soft clipping / limiter to prevent distortion
            val limited = tanh(sum / Short.MAX_VALUE.toFloat()) * Short.MAX_VALUE.toFloat()
            mixedSamples[i] = limited.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }

        // Convert mixed samples back to PCM byte array
        val mixedPcm = ByteArray(totalMixSamples * 2)
        val outBuffer = ByteBuffer.wrap(mixedPcm).order(ByteOrder.LITTLE_ENDIAN)
        for (sample in mixedSamples) {
            outBuffer.putShort(sample)
        }

        val finalWavBytes = pcmToWav(mixedPcm, sampleRate = sampleRate, channels = 1)
        val audioDir = File(context.filesDir, "voxora_audio").apply { mkdirs() }
        val mixedFile = File(audioDir, outputFileName)
        FileOutputStream(mixedFile).use { fos ->
            fos.write(finalWavBytes)
        }
        mixedFile
    }

    /**
     * Generate pleasant formant speech approximation for Offline Demo Mode.
     */
    suspend fun generateDemoSpeechAudio(text: String, speed: Float, voiceName: String, outputFileName: String): File = withContext(Dispatchers.IO) {
        val sampleRate = 24000
        val charCount = text.length.coerceAtLeast(10)
        // ~15 characters per second at 1.0x speed
        val baseDurationSeconds = (charCount / (15.0f * speed)).coerceIn(2.5f, 60.0f)
        val totalSamples = (baseDurationSeconds * sampleRate).toInt()

        val basePitch = when {
            voiceName in listOf("Charon", "Fenrir", "Titan", "Oberon") -> 120.0
            voiceName in listOf("Kore", "Aoede", "Leda", "Io", "Lyra") -> 240.0
            else -> 175.0
        }

        val samples = ShortArray(totalSamples)
        var phase = 0.0

        for (i in 0 until totalSamples) {
            val t = i.toDouble() / sampleRate
            // Syllable cadence modulation (~4 syllables per second)
            val syllableEnvelope = (0.5 + 0.5 * sin(2.0 * PI * 4.2 * t)).pow(1.5)
            // Intonation pitch contour
            val pitchContour = basePitch + 15.0 * sin(2.0 * PI * 0.8 * t)

            phase += 2.0 * PI * pitchContour / sampleRate
            if (phase > 2.0 * PI) phase -= 2.0 * PI

            // Formants (Vowel resonance approximation F1, F2)
            val harmonic1 = sin(phase)
            val harmonic2 = 0.6 * sin(2.0 * phase)
            val harmonic3 = 0.3 * sin(3.0 * phase)
            val harmonic4 = 0.15 * sin(4.0 * phase)

            val raw = (harmonic1 + harmonic2 + harmonic3 + harmonic4) * syllableEnvelope * 0.4
            val sampleVal = (raw * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
            samples[i] = sampleVal.toShort()
        }

        val pcmBytes = ByteArray(totalSamples * 2)
        val buffer = ByteBuffer.wrap(pcmBytes).order(ByteOrder.LITTLE_ENDIAN)
        for (s in samples) {
            buffer.putShort(s)
        }

        val wavBytes = pcmToWav(pcmBytes, sampleRate = sampleRate, channels = 1)
        val audioDir = File(context.filesDir, "voxora_audio").apply { mkdirs() }
        val demoFile = File(audioDir, outputFileName)
        FileOutputStream(demoFile).use { it.write(wavBytes) }
        demoFile
    }

    /**
     * Compute visual amplitude waveform points (e.g. 80 normalized bars) for UI timeline.
     */
    fun extractWaveformPoints(audioFile: File, pointCount: Int = 80): List<Float> {
        try {
            if (!audioFile.exists() || audioFile.length() < 100) {
                return List(pointCount) { 0.15f }
            }
            val bytes = audioFile.readBytes()
            val pcm = extractPcmFromWav(bytes)
            val numSamples = pcm.size / 2
            if (numSamples < pointCount) {
                return List(pointCount) { 0.2f }
            }

            val samplesPerPoint = numSamples / pointCount
            val buffer = ByteBuffer.wrap(pcm).order(ByteOrder.LITTLE_ENDIAN)
            val points = mutableListOf<Float>()

            for (p in 0 until pointCount) {
                var maxAmp = 0f
                for (s in 0 until samplesPerPoint) {
                    if (buffer.hasRemaining()) {
                        val amp = abs(buffer.short.toFloat()) / Short.MAX_VALUE.toFloat()
                        if (amp > maxAmp) maxAmp = amp
                    }
                }
                // Normalize and apply minimal height for aesthetics
                val barHeight = (maxAmp * 1.3f).coerceIn(0.08f, 1.0f)
                points.add(barHeight)
            }
            return points
        } catch (e: Exception) {
            return List(pointCount) { 0.2f }
        }
    }

    /**
     * Playback controller.
     */
    fun playAudio(filePath: String, speed: Float = 1.0f, onProgress: (Int, Int) -> Unit = { _, _ -> }) {
        try {
            if (currentPlayingPath == filePath && mediaPlayer != null && isPrepared) {
                mediaPlayer?.start()
                return
            }

            stopPlayback()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(filePath)
                prepare()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    playbackParams = playbackParams.setSpeed(speed.coerceIn(0.5f, 2.0f))
                }
                setOnCompletionListener {
                    onCompletionListener?.invoke()
                }
                setOnErrorListener { _, what, extra ->
                    onErrorListener?.invoke("Playback error: $what, $extra")
                    true
                }
                start()
            }
            currentPlayingPath = filePath
            isPrepared = true
        } catch (e: Exception) {
            onErrorListener?.invoke("Audio playback error: ${e.message}")
        }
    }

    fun pausePlayback() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun resumePlayback() {
        try {
            mediaPlayer?.start()
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun stopPlayback() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) it.stop()
                it.reset()
                it.release()
            }
            mediaPlayer = null
            isPrepared = false
            currentPlayingPath = null
        } catch (e: Exception) {
            mediaPlayer = null
        }
    }

    fun seekTo(positionMs: Int) {
        try {
            mediaPlayer?.seekTo(positionMs)
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying == true

    fun getCurrentPosition(): Int = try { mediaPlayer?.currentPosition ?: 0 } catch (e: Exception) { 0 }

    fun getDuration(): Int = try { mediaPlayer?.duration ?: 0 } catch (e: Exception) { 0 }

    fun setSpeed(speed: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mediaPlayer != null && isPrepared) {
            try {
                mediaPlayer?.playbackParams = mediaPlayer?.playbackParams?.setSpeed(speed.coerceIn(0.5f, 2.0f)) ?: PlaybackParams().setSpeed(speed)
            } catch (e: Exception) {
                // Ignore
            }
        }
    }
}
