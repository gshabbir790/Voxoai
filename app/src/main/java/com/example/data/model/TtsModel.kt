package com.example.data.model

enum class ModelCategory {
    FAST,
    PROFESSIONAL,
    PREMIUM
}

data class TtsModel(
    val id: String,
    val name: String,
    val category: ModelCategory,
    val quality: String,
    val description: String,
    val recommendedUse: String,
    val maxInputCharacters: Int,
    val supportsPitch: Boolean,
    val supportsSpeed: Boolean,
    val supportsStreaming: Boolean,
    val supportsEmotionDirection: Boolean,
    val isAvailable: Boolean = true
) {
    companion object {
        val GEMINI_TTS_PRIMARY = TtsModel(
            id = "gemini-2.5-flash-preview-tts",
            name = "Gemini 2.5 Flash TTS (Studio)",
            category = ModelCategory.PROFESSIONAL,
            quality = "Studio Ultra-HD (24kHz PCM/WAV)",
            description = "Current official Google Gemini TTS model with native speech synthesis, low latency, and rich prosody.",
            recommendedUse = "Long-form narration, YouTube videos, documentaries, e-learning, commercials.",
            maxInputCharacters = 8000,
            supportsPitch = true,
            supportsSpeed = true,
            supportsStreaming = true,
            supportsEmotionDirection = true,
            isAvailable = true
        )

        val GEMINI_NATIVE_AUDIO = TtsModel(
            id = "gemini-2.5-flash-native-audio-preview-12-2025",
            name = "Gemini 2.5 Flash Native Audio",
            category = ModelCategory.FAST,
            quality = "Real-Time HD Audio",
            description = "Ultra-fast response audio model optimized for social clips, Shorts, Reels, and instant speech.",
            recommendedUse = "TikTok, Shorts, quick announcements, social media voiceovers.",
            maxInputCharacters = 4000,
            supportsPitch = true,
            supportsSpeed = true,
            supportsStreaming = true,
            supportsEmotionDirection = true,
            isAvailable = true
        )

        val GEMINI_SCRIPT_DIRECTOR = TtsModel(
            id = "gemini-3.5-flash",
            name = "Gemini 3.5 Flash (AI Director)",
            category = ModelCategory.PREMIUM,
            quality = "Multi-Token Reasoning Engine",
            description = "Dedicated AI script intelligence engine for emotional analysis, scene pacing, and audio direction.",
            recommendedUse = "AI Voice Director, script analysis, pause optimization, style recommendation.",
            maxInputCharacters = 32000,
            supportsPitch = false,
            supportsSpeed = false,
            supportsStreaming = false,
            supportsEmotionDirection = false,
            isAvailable = true
        )

        val AVAILABLE_MODELS = listOf(GEMINI_TTS_PRIMARY, GEMINI_NATIVE_AUDIO)
        val ALL_MODELS = listOf(GEMINI_TTS_PRIMARY, GEMINI_NATIVE_AUDIO, GEMINI_SCRIPT_DIRECTOR)

        fun findById(id: String): TtsModel = ALL_MODELS.firstOrNull { it.id == id } ?: GEMINI_TTS_PRIMARY
    }
}
