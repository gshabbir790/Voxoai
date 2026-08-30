package com.example.data.model

data class AiDirectorRecommendation(
    val suggestedVoiceName: String,
    val suggestedModelId: String,
    val suggestedStyle: String,
    val suggestedPrimaryEmotion: Emotion,
    val suggestedSecondaryEmotion: Emotion,
    val emotionIntensity: Int,
    val suggestedSpeed: Float,
    val suggestedPitch: PitchSetting,
    val suggestedAccent: String,
    val suggestedMusicTrackId: String,
    val sceneBreakPoints: List<String>,
    val importantKeywords: List<String>,
    val pauseSuggestions: List<String>,
    val reasoningRationale: String
)
