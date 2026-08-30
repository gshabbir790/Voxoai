package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usage_stats")
data class UsageStats(
    @PrimaryKey val id: Int = 1,
    val totalCharactersProcessed: Long = 0,
    val totalAudioSecondsGenerated: Float = 0f,
    val totalProjectsCreated: Int = 0,
    val creditLimitCharacters: Long = 100000,
    val subscriptionTier: String = "PRO" // FREE, PRO, BUSINESS
)
