package com.example.data.api
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generateContent
import android.content.Context
import com.example.BuildConfig
import com.example.data.audio.AudioEngine
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.util.UUID

sealed class TtsGenerationResult {
    data class Success(
        val voiceOnlyFile: File,
        val mixedAudioFile: File,
        val durationSeconds: Float,
        val charactersProcessed: Int,
        val isDemoMode: Boolean = false
    ) : TtsGenerationResult()

    data class Progress(
        val stepTitle: String,
        val currentStep: Int,
        val totalSteps: Int,
        val percent: Float
    ) : TtsGenerationResult()

    data class Error(
        val message: String,
        val isQuotaExceeded: Boolean = false,
        val canRetry: Boolean = true
    ) : TtsGenerationResult()
}

class GeminiTtsProvider(
    private val context: Context,
    private val apiService: GeminiApiService,
    private val audioEngine: AudioEngine
) {

    /**
     * Check if valid Gemini API key is configured.
     */
    fun hasValidApiKey(): Boolean {
        return try {
            val key = BuildConfig.GEMINI_API_KEY
            key.isNotBlank() && key != "MY_GEMINI_API_KEY" && !key.contains("PLACEHOLDER", ignoreCase = true)
        } catch (e: Exception) {
            false
        }
    }

    private fun getApiKey(): String {
        return try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Clean script for speech synthesis so instruction tags are converted to natural pauses.
     */
    fun sanitizeScriptForTts(rawText: String): String {
        var clean = rawText
            // Replace pauses with natural punctuation ellipses/dashes
            .replace(Regex("\\[pause\\s*([0-9.]+s)?\\]", RegexOption.IGNORE_CASE), ", ... ")
            .replace(Regex("\\[emphasis:\\s*([^\\]]+)\\]", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\[pronounce:\\s*([^\\]]+)\\]", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\[[^\\]]+\\]"), "")
            .trim()
        return clean
    }

    /**
     * Generate complete voice project with scenes, music, and auto-ducking.
     */
    suspend fun generateVoiceProject(
        script: String,
        language: Language,
        model: TtsModel,
        voice: GeminiVoice,
        style: SpeakingStyle,
        emotionBlend: EmotionBlend,
        controls: SpeakingControls,
        accent: String,
        customVoiceDirection: String,
        musicTrack: MusicTrack,
        voiceVolume: Float,
        musicVolume: Float,
        autoDucking: Boolean,
        duckingAmountPercent: Int,
        duckingAttackMs: Int,
        duckingReleaseMs: Int,
        onProgress: (TtsGenerationResult.Progress) -> Unit
    ): TtsGenerationResult = withContext(Dispatchers.IO) {
        val sanitizedText = sanitizeScriptForTts(script)
        if (sanitizedText.isBlank()) {
            return@withContext TtsGenerationResult.Error("Text is empty. Please enter your script to generate voice.")
        }

        onProgress(TtsGenerationResult.Progress("Preparing Script & Direction", 1, 5, 0.2f))
        delay(200)

        val isDemo = !hasValidApiKey()

        val uniqueId = UUID.randomUUID().toString().take(8)
        val voiceFileName = "voxora_voice_${uniqueId}.wav"
        val mixedFileName = "voxora_mixed_${uniqueId}.wav"

        // Construct speaking direction system instructions
        val promptDirection = buildString {
            append("Speak in ${language.displayName} (${language.nativeName}). ")
            append("Voice tone: ${voice.characterTitle}. ")
            append("Speaking Style: ${style.name} (${style.description}). ")
            append(emotionBlend.toPromptDirection()).append(" ")
            append(controls.toPromptDirection()).append(" ")
            if (accent.isNotBlank() && accent != "Neutral") {
                append("Accent nuance: $accent. ")
            }
            if (customVoiceDirection.isNotBlank()) {
                append("Director Notes: $customVoiceDirection. ")
            }
            append("Do not read any formatting or bracketed notes aloud. Deliver high quality expressive natural voice.")
        }

        val generatedVoiceFile: File = if (isDemo) {
            onProgress(TtsGenerationResult.Progress("Generating Voice (Demo Mode Preview)", 2, 5, 0.45f))
            delay(500)
            audioEngine.generateDemoSpeechAudio(
                text = sanitizedText,
                speed = controls.speed,
                voiceName = voice.name,
                outputFileName = voiceFileName
            )
        } else {
            // Real Gemini TTS API Call with Retries and Chunking
            onProgress(TtsGenerationResult.Progress("Generating AI Voice with ${model.name}", 2, 5, 0.45f))

            try {
                generateGeminiTtsWithRetry(
                    text = sanitizedText,
                    modelId = model.id,
                    voiceName = voice.name,
                    promptDirection = promptDirection,
                    outputFileName = voiceFileName
                )
            } catch (e: Exception) {
                // If API quota or network error occurs, return error with retry affordance
                val isQuota = e.message?.contains("quota", ignoreCase = true) == true || e.message?.contains("429") == true
                return@withContext TtsGenerationResult.Error(
                    message = if (isQuota) "Your API quota has been reached. Please check your Gemini API billing."
                              else "Audio generation failed: ${e.localizedMessage ?: "Network or API timeout."}. Please retry.",
                    isQuotaExceeded = isQuota,
                    canRetry = true
                )
            }
        }

        onProgress(TtsGenerationResult.Progress("Processing & Normalizing Audio", 3, 5, 0.70f))
        delay(200)

        // Mix background music with intelligent Auto Ducking
        onProgress(TtsGenerationResult.Progress("Mixing Background Music (${musicTrack.title})", 4, 5, 0.88f))
        val mixedFile = audioEngine.mixVoiceAndMusic(
            voiceWavFile = generatedVoiceFile,
            musicTrack = musicTrack,
            voiceVolume = voiceVolume,
            musicVolume = musicVolume,
            autoDucking = autoDucking,
            duckingAmountPercent = duckingAmountPercent,
            duckingAttackMs = duckingAttackMs,
            duckingReleaseMs = duckingReleaseMs,
            outputFileName = mixedFileName
        )

        onProgress(TtsGenerationResult.Progress("Finalizing Master Audio", 5, 5, 1.0f))
        delay(150)

        val durationSec = (generatedVoiceFile.length().toFloat() / (24000f * 2f)).coerceAtLeast(1.0f)

        TtsGenerationResult.Success(
            voiceOnlyFile = generatedVoiceFile,
            mixedAudioFile = mixedFile,
            durationSeconds = durationSec,
            charactersProcessed = sanitizedText.length,
            isDemoMode = isDemo
        )
    }

    /**
     * Gemini TTS API call with 3 automatic retries and exponential backoff.
     */
    private suspend fun generateGeminiTtsWithRetry(
        text: String,
        modelId: String,
        voiceName: String,
        promptDirection: String,
        outputFileName: String
    ): File {
        val apiKey = getApiKey()
        var lastException: Exception? = null

        // Chunks for long text if necessary
        val chunks = splitTextIntoChunks(text, maxChars = 3500)
        val tempFiles = mutableListOf<File>()

        for ((index, chunk) in chunks.withIndex()) {
            var attempt = 0
            var success = false
            var chunkFile: File? = null

            while (attempt < 3 && !success) {
                attempt++
                try {
                    val request = GeminiGenerateRequest(
                        contents = listOf(
                            GeminiContent(
                                parts = listOf(GeminiPart(text = chunk)),
                                role = "user"
                            )
                        ),
                        generationConfig = GeminiGenerationConfig(
                            responseModalities = listOf("AUDIO"),
                            speechConfig = GeminiSpeechConfig(
                                voiceConfig = GeminiVoiceConfig(
                                    prebuiltVoiceConfig = GeminiPrebuiltVoiceConfig(voiceName = voiceName)
                                )
                            ),
                            temperature = 0.7f
                        ),
                        systemInstruction = GeminiContent(
                            parts = listOf(GeminiPart(text = promptDirection))
                        )
                    )

                    val response = apiService.generateContent(
                        model = modelId,
                        apiKey = apiKey,
                        request = request
                    )

                    if (response.error != null) {
                        throw Exception("Gemini API Error: ${response.error.message ?: response.error.status}")
                    }

                    val candidate = response.candidates?.firstOrNull()
                    val audioPart = candidate?.content?.parts?.firstOrNull { it.inlineData != null }

                    if (audioPart?.inlineData != null) {
                        val chunkName = "chunk_${index}_$outputFileName"
                        chunkFile = audioEngine.saveBase64AudioToFile(
                            base64Data = audioPart.inlineData.data,
                            mimeType = audioPart.inlineData.mimeType,
                            outputFileName = chunkName
                        )
                        tempFiles.add(chunkFile)
                        success = true
                    } else {
                        throw Exception("No audio data returned by Gemini TTS")
                    }
                } catch (e: Exception) {
                    lastException = e
                    if (attempt < 3) {
                        delay((attempt * 1000L)) // Exponential backoff: 1s, 2s
                    }
                }
            }

            if (!success || chunkFile == null) {
                throw lastException ?: Exception("Voice generation failed after 3 attempts.")
            }
        }

        // If single chunk, return it as output file
        if (tempFiles.size == 1) {
            val finalTarget = File(context.filesDir, "voxora_audio/$outputFileName")
            tempFiles[0].copyTo(finalTarget, overwrite = true)
            return finalTarget
        }

        // Merge multiple chunks
        val mergedTarget = File(context.filesDir, "voxora_audio/$outputFileName")
        mergeWavFiles(tempFiles, mergedTarget)
        return mergedTarget
    }

    private fun splitTextIntoChunks(text: String, maxChars: Int): List<String> {
        if (text.length <= maxChars) return listOf(text)
        val chunks = mutableListOf<String>()
        val sentences = text.split(Regex("(?<=[.!?۔\n])\\s+"))
        var current = StringBuilder()

        for (s in sentences) {
            if (current.length + s.length > maxChars && current.isNotEmpty()) {
                chunks.add(current.toString().trim())
                current = StringBuilder()
            }
            current.append(s).append(" ")
        }
        if (current.isNotEmpty()) {
            chunks.add(current.toString().trim())
        }
        return if (chunks.isEmpty()) listOf(text) else chunks
    }

    private fun mergeWavFiles(files: List<File>, outputFile: File) {
        val pcmList = mutableListOf<ByteArray>()
        for (f in files) {
            val bytes = f.readBytes()
            val pcm = if (bytes.size > 44) bytes.copyOfRange(44, bytes.size) else bytes
            pcmList.add(pcm)
        }
        val totalPcmSize = pcmList.sumOf { it.size }
        val mergedPcm = ByteArray(totalPcmSize)
        var offset = 0
        for (pcm in pcmList) {
            System.arraycopy(pcm, 0, mergedPcm, offset, pcm.size)
            offset += pcm.size
        }
        val wavBytes = audioEngine.pcmToWav(mergedPcm, sampleRate = 24000, channels = 1)
        outputFile.writeBytes(wavBytes)
    }

    /**
     * AI Script Director: Analyzes script text with Gemini to recommend voice, style, emotions, pacing, and music.
     */
    suspend fun analyzeScriptWithAiDirector(
        script: String,
        language: Language
    ): AiDirectorRecommendation = withContext(Dispatchers.IO) {
        if (!hasValidApiKey()) {
            // Intelligent local heuristic analysis in Demo Mode
            val isUrdu = language == Language.URDU || script.any { it in '\u0600'..'\u06FF' }
            val isDoc = script.contains("تاریخ", ignoreCase = true) || script.contains("history", ignoreCase = true) || script.contains("documentary", ignoreCase = true)
            val isEmotional = script.contains("امید", ignoreCase = true) || script.contains("دل", ignoreCase = true) || script.contains("love", ignoreCase = true) || script.contains("hope", ignoreCase = true)

            return@withContext when {
                isDoc -> AiDirectorRecommendation(
                    suggestedVoiceName = "Charon",
                    suggestedModelId = "gemini-2.5-flash-preview-tts",
                    suggestedStyle = "Historical Documentary",
                    suggestedPrimaryEmotion = Emotion.SERIOUS,
                    suggestedSecondaryEmotion = Emotion.DRAMATIC,
                    emotionIntensity = 85,
                    suggestedSpeed = 0.90f,
                    suggestedPitch = PitchSetting.LOW,
                    suggestedAccent = if (isUrdu) "Pakistani Urdu" else "Neutral English",
                    suggestedMusicTrackId = "documentary_deep",
                    sceneBreakPoints = listOf("Introduction", "Historical Arc", "Conclusion"),
                    importantKeywords = listOf("تاریخ", "عظیم", "تمدن", "حکمت"),
                    pauseSuggestions = listOf("Insert 1.0s pause after intro", "Insert 0.5s pause before conclusion"),
                    reasoningRationale = "Script features historical and solemn narrative markers suitable for deep, gravitas baritone voice with atmospheric documentary backdrop."
                )
                isEmotional -> AiDirectorRecommendation(
                    suggestedVoiceName = "Lyra",
                    suggestedModelId = "gemini-2.5-flash-preview-tts",
                    suggestedStyle = "Emotional Speech",
                    suggestedPrimaryEmotion = Emotion.WARM,
                    suggestedSecondaryEmotion = Emotion.EMOTIONAL,
                    emotionIntensity = 80,
                    suggestedSpeed = 0.85f,
                    suggestedPitch = PitchSetting.NORMAL,
                    suggestedAccent = if (isUrdu) "Pakistani Urdu" else "Neutral English",
                    suggestedMusicTrackId = "emotional_piano",
                    sceneBreakPoints = listOf("Vulnerability", "Climax", "Inspiration"),
                    importantKeywords = listOf("امید", "روشنی", "صبر", "یقین"),
                    pauseSuggestions = listOf("Insert 0.8s pause at turning point"),
                    reasoningRationale = "Script carries tender emotive themes ideal for a warm, vulnerable delivery backed by soft piano harmonies."
                )
                else -> AiDirectorRecommendation(
                    suggestedVoiceName = "Kore",
                    suggestedModelId = "gemini-2.5-flash-preview-tts",
                    suggestedStyle = "Storytelling",
                    suggestedPrimaryEmotion = Emotion.CALM,
                    suggestedSecondaryEmotion = Emotion.FRIENDLY,
                    emotionIntensity = 75,
                    suggestedSpeed = 1.00f,
                    suggestedPitch = PitchSetting.NORMAL,
                    suggestedAccent = language.supportedAccents.firstOrNull() ?: "Neutral",
                    suggestedMusicTrackId = "cinematic_ambient",
                    sceneBreakPoints = listOf("Opening", "Body", "Closing"),
                    importantKeywords = listOf("Vision", "Future", "Clarity"),
                    pauseSuggestions = listOf("Insert natural pauses at sentence boundaries"),
                    reasoningRationale = "Balanced storytelling cadence with natural conversational breathing and clean cinematic ambient bed."
                )
            }
        }

        // Live Gemini API Analysis with gemini-3.5-flash
        try {
            val analysisPrompt = """
                You are the AI Voice Director for VOXORA AI STUDIO.
                Analyze the following script written in ${language.displayName}:
                "$script"

                Recommend the optimal voice settings formatted STRICTLY in valid JSON:
                {
                    "suggestedVoiceName": "Charon or Kore or Fenrir or Vega or Leda or Lyra or Enceladus or Perseus or Puck or Iapetus",
                    "suggestedStyle": "Documentary or Commercial or Emotional Speech or Educational or News Anchor or Conversational",
                    "suggestedPrimaryEmotion": "Serious or Warm or Confident or Calm or Sad or Hopeful or Dramatic",
                    "suggestedSecondaryEmotion": "Dramatic or Emotional or Energetic or Informative",
                    "emotionIntensity": 80,
                    "suggestedSpeed": 0.95,
                    "suggestedPitch": "Low or Normal or High",
                    "suggestedAccent": "${language.supportedAccents.firstOrNull() ?: "Neutral"}",
                    "suggestedMusicTrackId": "cinematic_ambient or documentary_deep or emotional_piano or corporate_uplift or news_bed_subtle or none",
                    "sceneBreakPoints": ["Scene 1", "Scene 2"],
                    "importantKeywords": ["Keyword 1", "Keyword 2"],
                    "pauseSuggestions": ["Pause suggestion 1"],
                    "reasoningRationale": "Brief 1-sentence director rationale"
                }
            """.trimIndent()

            val request = GeminiGenerateRequest(
                contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = analysisPrompt))))
            )

            val response = apiService.generateContent(
                model = "gemini-3.5-flash",
                apiKey = getApiKey(),
                request = request
            )

            val rawText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
            val jsonString = rawText.substringAfter("{").substringBeforeLast("}")
            val fullJson = "{$jsonString}"
            val json = JSONObject(fullJson)

            AiDirectorRecommendation(
                suggestedVoiceName = json.optString("suggestedVoiceName", "Kore"),
                suggestedModelId = "gemini-2.5-flash-preview-tts",
                suggestedStyle = json.optString("suggestedStyle", "Documentary"),
                suggestedPrimaryEmotion = Emotion.fromLabel(json.optString("suggestedPrimaryEmotion", "Calm")),
                suggestedSecondaryEmotion = Emotion.fromLabel(json.optString("suggestedSecondaryEmotion", "Serious")),
                emotionIntensity = json.optInt("emotionIntensity", 80),
                suggestedSpeed = json.optDouble("suggestedSpeed", 1.0).toFloat(),
                suggestedPitch = PitchSetting.fromLabel(json.optString("suggestedPitch", "Normal")),
                suggestedAccent = json.optString("suggestedAccent", language.supportedAccents.firstOrNull() ?: "Neutral"),
                suggestedMusicTrackId = json.optString("suggestedMusicTrackId", "cinematic_ambient"),
                sceneBreakPoints = parseJsonArrayToList(json.optJSONArray("sceneBreakPoints")),
                importantKeywords = parseJsonArrayToList(json.optJSONArray("importantKeywords")),
                pauseSuggestions = parseJsonArrayToList(json.optJSONArray("pauseSuggestions")),
                reasoningRationale = json.optString("reasoningRationale", "Optimized pacing and prosody tailored for high resonance.")
            )
        } catch (e: Exception) {
            // Graceful fallback
            AiDirectorRecommendation(
                suggestedVoiceName = "Kore",
                suggestedModelId = "gemini-2.5-flash-preview-tts",
                suggestedStyle = "Documentary",
                suggestedPrimaryEmotion = Emotion.CALM,
                suggestedSecondaryEmotion = Emotion.SERIOUS,
                emotionIntensity = 75,
                suggestedSpeed = 1.00f,
                suggestedPitch = PitchSetting.NORMAL,
                suggestedAccent = language.supportedAccents.firstOrNull() ?: "Neutral",
                suggestedMusicTrackId = "cinematic_ambient",
                sceneBreakPoints = listOf("Intro", "Main Narration"),
                importantKeywords = listOf("Voice", "Clarity"),
                pauseSuggestions = listOf("Insert natural breathing pauses"),
                reasoningRationale = "Balanced voice direction with standard pacing and calm cinematic music."
            )
        }
    }

    private fun parseJsonArrayToList(array: org.json.JSONArray?): List<String> {
        if (array == null) return emptyList()
        val list = mutableListOf<String>()
        for (i in 0 until array.length()) {
            list.add(array.optString(i))
        }
        return list
    }
}
