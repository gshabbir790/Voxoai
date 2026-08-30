package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class VoiceProject(
    @PrimaryKey val id: String,
    val name: String,
    val script: String,
    val languageCode: String,
    val modelId: String,
    val voiceName: String,
    val accent: String,
    val styleName: String,
    val primaryEmotion: String,
    val primaryEmotionPercentage: Int,
    val secondaryEmotion: String,
    val secondaryEmotionPercentage: Int,
    val emotionIntensity: Int,
    val speed: Float,
    val pitch: String,
    val energy: String,
    val pausing: String,
    val customVoiceDirection: String,
    val musicTrackId: String,
    val voiceVolume: Float,
    val musicVolume: Float,
    val sfxVolume: Float,
    val autoDuckingEnabled: Boolean,
    val duckingAmountPercent: Int,
    val duckingAttackMs: Int,
    val duckingReleaseMs: Int,
    val scenesJson: String,
    val generatedAudioPath: String?,
    val voiceOnlyAudioPath: String?,
    val charactersProcessed: Int,
    val durationSeconds: Float,
    val isFavorite: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)
