package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiApiService
import com.example.data.api.GeminiTtsProvider
import com.example.data.api.TtsGenerationResult
import com.example.data.audio.AudioEngine
import com.example.data.db.AppDatabase
import com.example.data.db.ProjectRepository
import com.example.data.db.UsageStats
import com.example.data.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.UUID

enum class StudioScreen {
    STUDIO,
    PROJECTS,
    SAMPLES,
    USAGE,
    SETTINGS
}

data class StudioUiState(
    // Navigation
    val currentScreen: StudioScreen = StudioScreen.STUDIO,
    val isDarkTheme: Boolean = true,

    // Active Project
    val projectId: String = UUID.randomUUID().toString(),
    val projectName: String = "Untitled Voice Production",
    val scriptText: String = Language.URDU.placeholderText,
    val customVoiceDirection: String = "",
    val selectedLanguage: Language = Language.URDU,
    val selectedModel: TtsModel = TtsModel.GEMINI_TTS_PRIMARY,
    val selectedVoice: GeminiVoice = GeminiVoice.ALL_VOICES[0],
    val selectedAccent: String = "Pakistani Urdu",
    val selectedStyle: SpeakingStyle = SpeakingStyle.ALL_STYLES[0],
    val emotionBlend: EmotionBlend = EmotionBlend(Emotion.CONFIDENT, 100, Emotion.CALM, 0, 80),
    val speakingControls: SpeakingControls = SpeakingControls(1.00f, PitchSetting.NORMAL, EnergySetting.MEDIUM, PausingSetting.NATURAL),

    // Background Music & Audio Mixing
    val selectedMusicTrack: MusicTrack = MusicTrack.BUILT_IN_TRACKS[1],
    val voiceVolume: Float = 1.0f,
    val musicVolume: Float = 0.28f,
    val sfxVolume: Float = 0.5f,
    val autoDuckingEnabled: Boolean = true,
    val duckingAmountPercent: Int = 75,
    val duckingAttackMs: Int = 100,
    val duckingReleaseMs: Int = 450,

    // Multi-Scene Script
    val scenes: List<SceneItem> = listOf(
        SceneItem(title = "Scene 1", text = Language.URDU.placeholderText, voiceName = "Charon", style = "Historical Documentary")
    ),
    val activeSceneIndex: Int = 0,
    val isMultiSceneMode: Boolean = false,

    // Generation State
    val isGenerating: Boolean = false,
    val generationProgress: TtsGenerationResult.Progress? = null,
    val generationError: String? = null,
    val isQuotaExceeded: Boolean = false,
    val isDemoModeActive: Boolean = false,

    // Generated Audio & Player
    val voiceOnlyAudioFile: File? = null,
    val mixedAudioFile: File? = null,
    val isPlayingMixedAudio: Boolean = true, // true = Mixed, false = Voice Only (A/B toggle)
    val isPlaying: Boolean = false,
    val currentPositionMs: Int = 0,
    val durationMs: Int = 0,
    val playerPlaybackSpeed: Float = 1.0f,
    val waveformPoints: List<Float> = List(70) { 0.15f },

    // AI Voice Director
    val isAnalyzingWithAi: Boolean = false,
    val showAiDirectorDialog: Boolean = false,
    val aiRecommendation: AiDirectorRecommendation? = null,

    // Modals & Dialogs
    val showMusicLibraryDialog: Boolean = false,
    val showExportDialog: Boolean = false,
    val showSampleProjectsDialog: Boolean = false,
    val showApiKeyInfoDialog: Boolean = false,
    val voiceFilterCategory: VoiceCategory? = null,
    val styleFilterCategory: StyleCategory? = null,
    val userNotificationMessage: String? = null
)

class StudioViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = ProjectRepository(db.projectDao())
    private val audioEngine = AudioEngine(application)
    private val apiService = GeminiApiService.create()
    val ttsProvider = GeminiTtsProvider(application, apiService, audioEngine)

    private val _uiState = MutableStateFlow(StudioUiState())
    val uiState: StateFlow<StudioUiState> = _uiState.asStateFlow()

    val allSavedProjects: StateFlow<List<VoiceProject>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val usageStats: StateFlow<UsageStats?> = repository.usageStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private var playbackPollJob: Job? = null

    init {
        val hasKey = ttsProvider.hasValidApiKey()
        _uiState.update { it.copy(isDemoModeActive = !hasKey) }

        // Setup audio engine listeners
        audioEngine.onCompletionListener = {
            _uiState.update { it.copy(isPlaying = false, currentPositionMs = it.durationMs) }
            stopProgressPolling()
        }
        audioEngine.onErrorListener = { errMsg ->
            _uiState.update { it.copy(isPlaying = false, userNotificationMessage = errMsg) }
            stopProgressPolling()
        }
    }

    fun setScreen(screen: StudioScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    fun setScriptText(text: String) {
        _uiState.update { state ->
            val updatedScenes = if (state.scenes.isNotEmpty() && state.activeSceneIndex in state.scenes.indices) {
                state.scenes.toMutableList().apply {
                    this[state.activeSceneIndex] = this[state.activeSceneIndex].copy(text = text)
                }
            } else state.scenes
            state.copy(scriptText = text, scenes = updatedScenes)
        }
    }

    fun setCustomVoiceDirection(direction: String) {
        _uiState.update { it.copy(customVoiceDirection = direction) }
    }

    fun setLanguage(language: Language) {
        _uiState.update { state ->
            val defaultAccent = language.supportedAccents.firstOrNull() ?: "Neutral"
            val placeholder = language.placeholderText
            state.copy(
                selectedLanguage = language,
                selectedAccent = defaultAccent,
                scriptText = if (state.scriptText.isBlank() || state.scriptText == state.selectedLanguage.placeholderText) placeholder else state.scriptText
            )
        }
    }

    fun setTtsModel(model: TtsModel) {
        _uiState.update { it.copy(selectedModel = model) }
    }

    fun setVoice(voice: GeminiVoice) {
        _uiState.update { it.copy(selectedVoice = voice) }
    }

    fun setAccent(accent: String) {
        _uiState.update { it.copy(selectedAccent = accent) }
    }

    fun setSpeakingStyle(style: SpeakingStyle) {
        _uiState.update { it.copy(selectedStyle = style) }
    }

    fun setPrimaryEmotion(emotion: Emotion, percent: Int = 100) {
        _uiState.update { state ->
            val blend = state.emotionBlend.copy(
                primaryEmotion = emotion,
                primaryPercentage = percent.coerceIn(0, 100)
            )
            state.copy(emotionBlend = blend)
        }
    }

    fun setSecondaryEmotion(emotion: Emotion, percent: Int) {
        _uiState.update { state ->
            val blend = state.emotionBlend.copy(
                secondaryEmotion = emotion,
                secondaryPercentage = percent.coerceIn(0, 100),
                primaryPercentage = (100 - percent).coerceIn(0, 100)
            )
            state.copy(emotionBlend = blend)
        }
    }

    fun setEmotionIntensity(intensity: Int) {
        _uiState.update { state ->
            state.copy(emotionBlend = state.emotionBlend.copy(intensity = intensity.coerceIn(0, 100)))
        }
    }

    fun setSpeakingControls(speed: Float, pitch: PitchSetting, energy: EnergySetting, pausing: PausingSetting) {
        _uiState.update { state ->
            state.copy(speakingControls = SpeakingControls(speed, pitch, energy, pausing))
        }
    }

    fun setMusicTrack(track: MusicTrack) {
        _uiState.update { it.copy(selectedMusicTrack = track) }
    }

    fun setAudioVolumes(voiceVol: Float, musicVol: Float, sfxVol: Float) {
        _uiState.update { it.copy(voiceVolume = voiceVol, musicVolume = musicVol, sfxVolume = sfxVol) }
    }

    fun setAutoDucking(enabled: Boolean, amountPercent: Int, attackMs: Int, releaseMs: Int) {
        _uiState.update {
            it.copy(
                autoDuckingEnabled = enabled,
                duckingAmountPercent = amountPercent,
                duckingAttackMs = attackMs,
                duckingReleaseMs = releaseMs
            )
        }
    }

    fun setVoiceFilterCategory(cat: VoiceCategory?) {
        _uiState.update { it.copy(voiceFilterCategory = cat) }
    }

    fun setStyleFilterCategory(cat: StyleCategory?) {
        _uiState.update { it.copy(styleFilterCategory = cat) }
    }

    fun insertScriptTag(tag: String) {
        val current = _uiState.value.scriptText
        val newText = if (current.endsWith(" ") || current.isEmpty()) "$current$tag " else "$current $tag "
        setScriptText(newText)
    }

    // Scenes Management
    fun addScene() {
        _uiState.update { state ->
            val newIndex = state.scenes.size + 1
            val newScene = SceneItem(
                title = "Scene $newIndex",
                text = "",
                voiceName = state.selectedVoice.name,
                style = state.selectedStyle.name,
                emotion = state.emotionBlend.primaryEmotion.label
            )
            val updatedList = state.scenes + newScene
            state.copy(scenes = updatedList, activeSceneIndex = updatedList.size - 1, isMultiSceneMode = true)
        }
    }

    fun selectScene(index: Int) {
        if (index in _uiState.value.scenes.indices) {
            val scene = _uiState.value.scenes[index]
            _uiState.update {
                it.copy(
                    activeSceneIndex = index,
                    scriptText = scene.text,
                    selectedVoice = GeminiVoice.findByName(scene.voiceName),
                    selectedStyle = SpeakingStyle.findByName(scene.style),
                    emotionBlend = it.emotionBlend.copy(primaryEmotion = Emotion.fromLabel(scene.emotion))
                )
            }
        }
    }

    fun duplicateScene(index: Int) {
        if (index in _uiState.value.scenes.indices) {
            val original = _uiState.value.scenes[index]
            val copy = original.copy(
                id = UUID.randomUUID().toString(),
                title = "${original.title} (Copy)"
            )
            val list = _uiState.value.scenes.toMutableList().apply { add(index + 1, copy) }
            _uiState.update { it.copy(scenes = list, activeSceneIndex = index + 1) }
        }
    }

    fun deleteScene(index: Int) {
        if (_uiState.value.scenes.size > 1 && index in _uiState.value.scenes.indices) {
            val list = _uiState.value.scenes.toMutableList().apply { removeAt(index) }
            val newIndex = (index - 1).coerceAtLeast(0)
            _uiState.update { it.copy(scenes = list, activeSceneIndex = newIndex, scriptText = list[newIndex].text) }
        }
    }

    // One-Click Presets
    fun applyOneClickPreset(preset: OneClickPreset) {
        val voice = GeminiVoice.findByName(preset.voiceName)
        val style = SpeakingStyle.findByName(preset.style)
        val music = MusicTrack.findById(preset.musicTrackId)

        _uiState.update { state ->
            state.copy(
                selectedVoice = voice,
                selectedStyle = style,
                emotionBlend = EmotionBlend(
                    primaryEmotion = preset.primaryEmotion,
                    primaryPercentage = 70,
                    secondaryEmotion = preset.secondaryEmotion,
                    secondaryPercentage = 30,
                    intensity = preset.emotionIntensity
                ),
                speakingControls = state.speakingControls.copy(
                    speed = preset.speed,
                    pitch = preset.pitch
                ),
                selectedMusicTrack = music,
                autoDuckingEnabled = preset.autoDucking,
                userNotificationMessage = "Applied Preset: ${preset.title}"
            )
        }
    }

    // Sample Projects
    fun loadSampleProject(sample: SampleProject) {
        val voice = GeminiVoice.findByName(sample.voiceName)
        val style = SpeakingStyle.findByName(sample.style)
        val music = MusicTrack.findById(sample.musicTrackId)

        _uiState.update { state ->
            state.copy(
                projectName = sample.title,
                scriptText = sample.script,
                selectedLanguage = sample.language,
                selectedVoice = voice,
                selectedStyle = style,
                emotionBlend = EmotionBlend(
                    primaryEmotion = sample.primaryEmotion,
                    primaryPercentage = 70,
                    secondaryEmotion = sample.secondaryEmotion,
                    secondaryPercentage = 30,
                    intensity = sample.emotionIntensity
                ),
                speakingControls = state.speakingControls.copy(
                    speed = sample.speed,
                    pitch = sample.pitch
                ),
                selectedMusicTrack = music,
                scenes = sample.scenes,
                activeSceneIndex = 0,
                currentScreen = StudioScreen.STUDIO,
                showSampleProjectsDialog = false,
                userNotificationMessage = "Loaded Sample: ${sample.title}"
            )
        }
    }

    // AI Script Director
    fun runAiScriptDirector() {
        val script = _uiState.value.scriptText
        val lang = _uiState.value.selectedLanguage
        if (script.isBlank()) {
            _uiState.update { it.copy(userNotificationMessage = "Please write or paste a script first.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isAnalyzingWithAi = true, showAiDirectorDialog = true) }
            val recommendation = ttsProvider.analyzeScriptWithAiDirector(script, lang)
            _uiState.update { it.copy(isAnalyzingWithAi = false, aiRecommendation = recommendation) }
        }
    }

    fun applyAiDirectorRecommendation(rec: AiDirectorRecommendation) {
        val voice = GeminiVoice.findByName(rec.suggestedVoiceName)
        val style = SpeakingStyle.findByName(rec.suggestedStyle)
        val music = MusicTrack.findById(rec.suggestedMusicTrackId)
        val model = TtsModel.findById(rec.suggestedModelId)

        _uiState.update { state ->
            state.copy(
                selectedVoice = voice,
                selectedStyle = style,
                selectedModel = model,
                selectedAccent = rec.suggestedAccent,
                emotionBlend = EmotionBlend(
                    primaryEmotion = rec.suggestedPrimaryEmotion,
                    primaryPercentage = 70,
                    secondaryEmotion = rec.suggestedSecondaryEmotion,
                    secondaryPercentage = 30,
                    intensity = rec.emotionIntensity
                ),
                speakingControls = state.speakingControls.copy(
                    speed = rec.suggestedSpeed,
                    pitch = rec.suggestedPitch
                ),
                selectedMusicTrack = music,
                showAiDirectorDialog = false,
                userNotificationMessage = "Applied AI Director Recommendations"
            )
        }
    }

    // Voice Generation
    fun generateVoiceStudioAudio() {
        val state = _uiState.value
        val fullScript = if (state.isMultiSceneMode && state.scenes.isNotEmpty()) {
            state.scenes.joinToString(" [pause 1.0s] ") { it.text }
        } else {
            state.scriptText
        }

        if (fullScript.isBlank()) {
            _uiState.update { it.copy(userNotificationMessage = "Script cannot be empty.") }
            return
        }

        audioEngine.stopPlayback()

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isGenerating = true,
                    generationError = null,
                    isQuotaExceeded = false,
                    generationProgress = TtsGenerationResult.Progress("Initializing Studio Engine", 1, 5, 0.1f)
                )
            }

            val result = ttsProvider.generateVoiceProject(
                script = fullScript,
                language = state.selectedLanguage,
                model = state.selectedModel,
                voice = state.selectedVoice,
                style = state.selectedStyle,
                emotionBlend = state.emotionBlend,
                controls = state.speakingControls,
                accent = state.selectedAccent,
                customVoiceDirection = state.customVoiceDirection,
                musicTrack = state.selectedMusicTrack,
                voiceVolume = state.voiceVolume,
                musicVolume = state.musicVolume,
                autoDucking = state.autoDuckingEnabled,
                duckingAmountPercent = state.duckingAmountPercent,
                duckingAttackMs = state.duckingAttackMs,
                duckingReleaseMs = state.duckingReleaseMs,
                onProgress = { progress ->
                    _uiState.update { it.copy(generationProgress = progress) }
                }
            )

            when (result) {
                is TtsGenerationResult.Success -> {
                    val activeFile = if (state.selectedMusicTrack.id != "none") result.mixedAudioFile else result.voiceOnlyFile
                    val waveform = audioEngine.extractWaveformPoints(activeFile)

                    _uiState.update {
                        it.copy(
                            isGenerating = false,
                            generationProgress = null,
                            voiceOnlyAudioFile = result.voiceOnlyFile,
                            mixedAudioFile = result.mixedAudioFile,
                            isPlayingMixedAudio = state.selectedMusicTrack.id != "none",
                            waveformPoints = waveform,
                            durationMs = (result.durationSeconds * 1000).toInt(),
                            currentPositionMs = 0,
                            userNotificationMessage = if (result.isDemoMode) "Generated Audio (Demo Mode Preview)" else "Voice Synthesis Complete!"
                        )
                    }

                    // Auto-play generated master audio
                    playAudio()

                    // Increment usage stats in Room DB
                    repository.incrementUsage(result.charactersProcessed, result.durationSeconds)

                    // Auto-save project state
                    autoSaveCurrentProject(result.voiceOnlyFile.absolutePath, result.mixedAudioFile.absolutePath, result.durationSeconds)
                }
                is TtsGenerationResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isGenerating = false,
                            generationProgress = null,
                            generationError = result.message,
                            isQuotaExceeded = result.isQuotaExceeded,
                            userNotificationMessage = result.message
                        )
                    }
                }
                is TtsGenerationResult.Progress -> {
                    _uiState.update { it.copy(generationProgress = result) }
                }
            }
        }
    }

    // Audio Playback
    fun playAudio() {
        val state = _uiState.value
        val targetFile = if (state.isPlayingMixedAudio && state.mixedAudioFile != null) state.mixedAudioFile else state.voiceOnlyAudioFile
        if (targetFile == null || !targetFile.exists()) {
            _uiState.update { it.copy(userNotificationMessage = "Generate voice audio first to play.") }
            return
        }

        audioEngine.playAudio(targetFile.absolutePath, state.playerPlaybackSpeed)
        _uiState.update { it.copy(isPlaying = true, durationMs = audioEngine.getDuration().coerceAtLeast(1000)) }
        startProgressPolling()
    }

    fun pauseAudio() {
        audioEngine.pausePlayback()
        _uiState.update { it.copy(isPlaying = false) }
        stopProgressPolling()
    }

    fun togglePlayPause() {
        if (_uiState.value.isPlaying) pauseAudio() else playAudio()
    }

    fun seekTo(positionMs: Int) {
        audioEngine.seekTo(positionMs)
        _uiState.update { it.copy(currentPositionMs = positionMs) }
    }

    fun setPlayerPlaybackSpeed(speed: Float) {
        audioEngine.setSpeed(speed)
        _uiState.update { it.copy(playerPlaybackSpeed = speed) }
    }

    fun toggleAudioTrackAB(playMixed: Boolean) {
        val wasPlaying = _uiState.value.isPlaying
        val currentPos = audioEngine.getCurrentPosition()
        audioEngine.stopPlayback()

        _uiState.update { it.copy(isPlayingMixedAudio = playMixed) }
        val targetFile = if (playMixed && _uiState.value.mixedAudioFile != null) _uiState.value.mixedAudioFile else _uiState.value.voiceOnlyAudioFile
        targetFile?.let {
            val waveform = audioEngine.extractWaveformPoints(it)
            _uiState.update { s -> s.copy(waveformPoints = waveform) }
            if (wasPlaying) {
                audioEngine.playAudio(it.absolutePath, _uiState.value.playerPlaybackSpeed)
                audioEngine.seekTo(currentPos)
                _uiState.update { s -> s.copy(isPlaying = true, currentPositionMs = currentPos) }
                startProgressPolling()
            }
        }
    }

    private fun startProgressPolling() {
        stopProgressPolling()
        playbackPollJob = viewModelScope.launch {
            while (_uiState.value.isPlaying) {
                val pos = audioEngine.getCurrentPosition()
                val dur = audioEngine.getDuration().coerceAtLeast(1)
                _uiState.update { it.copy(currentPositionMs = pos, durationMs = dur) }
                delay(100)
            }
        }
    }

    private fun stopProgressPolling() {
        playbackPollJob?.cancel()
        playbackPollJob = null
    }

    // Projects CRUD
    fun saveProjectWithName(name: String) {
        val state = _uiState.value
        val scenesJson = serializeScenesToJson(state.scenes)
        val project = VoiceProject(
            id = state.projectId,
            name = name.ifBlank { state.projectName },
            script = state.scriptText,
            languageCode = state.selectedLanguage.code,
            modelId = state.selectedModel.id,
            voiceName = state.selectedVoice.name,
            accent = state.selectedAccent,
            styleName = state.selectedStyle.name,
            primaryEmotion = state.emotionBlend.primaryEmotion.label,
            primaryEmotionPercentage = state.emotionBlend.primaryPercentage,
            secondaryEmotion = state.emotionBlend.secondaryEmotion.label,
            secondaryEmotionPercentage = state.emotionBlend.secondaryPercentage,
            emotionIntensity = state.emotionBlend.intensity,
            speed = state.speakingControls.speed,
            pitch = state.speakingControls.pitch.label,
            energy = state.speakingControls.energy.label,
            pausing = state.speakingControls.pausing.label,
            customVoiceDirection = state.customVoiceDirection,
            musicTrackId = state.selectedMusicTrack.id,
            voiceVolume = state.voiceVolume,
            musicVolume = state.musicVolume,
            sfxVolume = state.sfxVolume,
            autoDuckingEnabled = state.autoDuckingEnabled,
            duckingAmountPercent = state.duckingAmountPercent,
            duckingAttackMs = state.duckingAttackMs,
            duckingReleaseMs = state.duckingReleaseMs,
            scenesJson = scenesJson,
            generatedAudioPath = state.mixedAudioFile?.absolutePath,
            voiceOnlyAudioPath = state.voiceOnlyAudioFile?.absolutePath,
            charactersProcessed = state.scriptText.length,
            durationSeconds = state.durationMs / 1000f,
            isFavorite = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.saveProject(project)
            _uiState.update { it.copy(projectName = name, userNotificationMessage = "Project '$name' saved successfully.") }
        }
    }

    private fun autoSaveCurrentProject(voicePath: String, mixedPath: String, durationSec: Float) {
        val state = _uiState.value
        val scenesJson = serializeScenesToJson(state.scenes)
        val project = VoiceProject(
            id = state.projectId,
            name = state.projectName,
            script = state.scriptText,
            languageCode = state.selectedLanguage.code,
            modelId = state.selectedModel.id,
            voiceName = state.selectedVoice.name,
            accent = state.selectedAccent,
            styleName = state.selectedStyle.name,
            primaryEmotion = state.emotionBlend.primaryEmotion.label,
            primaryEmotionPercentage = state.emotionBlend.primaryPercentage,
            secondaryEmotion = state.emotionBlend.secondaryEmotion.label,
            secondaryEmotionPercentage = state.emotionBlend.secondaryPercentage,
            emotionIntensity = state.emotionBlend.intensity,
            speed = state.speakingControls.speed,
            pitch = state.speakingControls.pitch.label,
            energy = state.speakingControls.energy.label,
            pausing = state.speakingControls.pausing.label,
            customVoiceDirection = state.customVoiceDirection,
            musicTrackId = state.selectedMusicTrack.id,
            voiceVolume = state.voiceVolume,
            musicVolume = state.musicVolume,
            sfxVolume = state.sfxVolume,
            autoDuckingEnabled = state.autoDuckingEnabled,
            duckingAmountPercent = state.duckingAmountPercent,
            duckingAttackMs = state.duckingAttackMs,
            duckingReleaseMs = state.duckingReleaseMs,
            scenesJson = scenesJson,
            generatedAudioPath = mixedPath,
            voiceOnlyAudioPath = voicePath,
            charactersProcessed = state.scriptText.length,
            durationSeconds = durationSec,
            isFavorite = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repository.saveProject(project)
        }
    }

    fun loadSavedProject(p: VoiceProject) {
        val lang = Language.fromCode(p.languageCode)
        val model = TtsModel.findById(p.modelId)
        val voice = GeminiVoice.findByName(p.voiceName)
        val style = SpeakingStyle.findByName(p.styleName)
        val music = MusicTrack.findById(p.musicTrackId)
        val scenes = deserializeScenesFromJson(p.scenesJson)

        val voiceFile = p.voiceOnlyAudioPath?.let { File(it) }?.takeIf { it.exists() }
        val mixedFile = p.generatedAudioPath?.let { File(it) }?.takeIf { it.exists() }

        _uiState.update { state ->
            state.copy(
                projectId = p.id,
                projectName = p.name,
                scriptText = p.script,
                selectedLanguage = lang,
                selectedModel = model,
                selectedVoice = voice,
                selectedAccent = p.accent,
                selectedStyle = style,
                emotionBlend = EmotionBlend(
                    primaryEmotion = Emotion.fromLabel(p.primaryEmotion),
                    primaryPercentage = p.primaryEmotionPercentage,
                    secondaryEmotion = Emotion.fromLabel(p.secondaryEmotion),
                    secondaryPercentage = p.secondaryEmotionPercentage,
                    intensity = p.emotionIntensity
                ),
                speakingControls = SpeakingControls(
                    speed = p.speed,
                    pitch = PitchSetting.fromLabel(p.pitch),
                    energy = EnergySetting.fromLabel(p.energy),
                    pausing = PausingSetting.fromLabel(p.pausing)
                ),
                customVoiceDirection = p.customVoiceDirection,
                selectedMusicTrack = music,
                voiceVolume = p.voiceVolume,
                musicVolume = p.musicVolume,
                sfxVolume = p.sfxVolume,
                autoDuckingEnabled = p.autoDuckingEnabled,
                duckingAmountPercent = p.duckingAmountPercent,
                duckingAttackMs = p.duckingAttackMs,
                duckingReleaseMs = p.duckingReleaseMs,
                scenes = if (scenes.isNotEmpty()) scenes else listOf(SceneItem(text = p.script)),
                voiceOnlyAudioFile = voiceFile,
                mixedAudioFile = mixedFile,
                currentScreen = StudioScreen.STUDIO,
                userNotificationMessage = "Loaded project: ${p.name}"
            )
        }
    }

    fun deleteProject(id: String) {
        viewModelScope.launch {
            repository.deleteProject(id)
            _uiState.update { it.copy(userNotificationMessage = "Project deleted.") }
        }
    }

    fun toggleFavoriteProject(id: String, currentVal: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, currentVal)
        }
    }

    fun createNewProject() {
        val newId = UUID.randomUUID().toString()
        _uiState.update { state ->
            state.copy(
                projectId = newId,
                projectName = "New Voice Studio Project",
                scriptText = state.selectedLanguage.placeholderText,
                voiceOnlyAudioFile = null,
                mixedAudioFile = null,
                scenes = listOf(SceneItem(title = "Scene 1", text = state.selectedLanguage.placeholderText)),
                activeSceneIndex = 0,
                currentScreen = StudioScreen.STUDIO,
                userNotificationMessage = "Created new project"
            )
        }
    }

    fun dismissNotification() {
        _uiState.update { it.copy(userNotificationMessage = null) }
    }

    fun setShowMusicLibraryDialog(show: Boolean) {
        _uiState.update { it.copy(showMusicLibraryDialog = show) }
    }

    fun setShowExportDialog(show: Boolean) {
        _uiState.update { it.copy(showExportDialog = show) }
    }

    fun setShowSampleProjectsDialog(show: Boolean) {
        _uiState.update { it.copy(showSampleProjectsDialog = show) }
    }

    fun setShowAiDirectorDialog(show: Boolean) {
        _uiState.update { it.copy(showAiDirectorDialog = show) }
    }

    private fun serializeScenesToJson(scenes: List<SceneItem>): String {
        val array = JSONArray()
        for (s in scenes) {
            val obj = JSONObject().apply {
                put("id", s.id)
                put("title", s.title)
                put("text", s.text)
                put("voiceName", s.voiceName)
                put("style", s.style)
                put("emotion", s.emotion)
                put("speed", s.speed.toDouble())
                put("pitch", s.pitch)
            }
            array.put(obj)
        }
        return array.toString()
    }

    private fun deserializeScenesFromJson(json: String): List<SceneItem> {
        return try {
            val array = JSONArray(json)
            val list = mutableListOf<SceneItem>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    SceneItem(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        title = obj.optString("title", "Scene ${i + 1}"),
                        text = obj.optString("text", ""),
                        voiceName = obj.optString("voiceName", "Kore"),
                        style = obj.optString("style", "Documentary"),
                        emotion = obj.optString("emotion", "Calm"),
                        speed = obj.optDouble("speed", 1.0).toFloat(),
                        pitch = obj.optString("pitch", "Normal")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioEngine.stopPlayback()
    }
}
