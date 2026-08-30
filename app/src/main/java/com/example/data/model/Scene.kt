package com.example.data.model

import java.util.UUID

data class SceneItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "Scene 1",
    val text: String = "",
    val voiceDirection: String = "",
    val voiceName: String = "Kore",
    val style: String = "Documentary",
    val emotion: String = "Calm",
    val emotionIntensity: Int = 75,
    val speed: Float = 1.00f,
    val pitch: String = "Normal",
    val musicTrackId: String = "none",
    val volume: Float = 1.0f,
    val audioDurationSeconds: Float = 0f,
    val generatedAudioPath: String? = null,
    val isGenerating: Boolean = false
)
