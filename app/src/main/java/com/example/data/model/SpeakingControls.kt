package com.example.data.model

enum class PitchSetting(val label: String, val multiplier: Float, val description: String) {
    LOW("Low", 0.85f, "Deeper, lower baritone register"),
    NORMAL("Normal", 1.00f, "Natural voice fundamental frequency"),
    HIGH("High", 1.15f, "Higher, brighter register");

    companion object {
        fun fromLabel(label: String): PitchSetting = values().firstOrNull { it.label.equals(label, ignoreCase = true) } ?: NORMAL
    }
}

enum class EnergySetting(val label: String, val description: String) {
    LOW("Low", "Subtle, intimate, and soft projection"),
    MEDIUM("Medium", "Standard natural studio projection"),
    HIGH("High", "Projected, resonant, and dynamic projection");

    companion object {
        fun fromLabel(label: String): EnergySetting = values().firstOrNull { it.label.equals(label, ignoreCase = true) } ?: MEDIUM
    }
}

enum class PausingSetting(val label: String, val description: String) {
    MINIMAL("Minimal", "Tight, fast continuous flow without extra pauses"),
    NATURAL("Natural", "Standard conversational breathing and punctuation pauses"),
    EXPRESSIVE("Expressive", "Deliberate, rhythmic pauses for emphasis and clarity"),
    DRAMATIC("Dramatic", "Long, suspenseful pauses that build anticipation");

    companion object {
        fun fromLabel(label: String): PausingSetting = values().firstOrNull { it.label.equals(label, ignoreCase = true) } ?: NATURAL
    }
}

data class SpeakingControls(
    val speed: Float = 1.00f,
    val pitch: PitchSetting = PitchSetting.NORMAL,
    val energy: EnergySetting = EnergySetting.MEDIUM,
    val pausing: PausingSetting = PausingSetting.NATURAL
) {
    companion object {
        val SPEED_OPTIONS = listOf(0.50f, 0.75f, 0.85f, 1.00f, 1.15f, 1.25f, 1.50f, 2.00f)
    }

    fun toPromptDirection(): String {
        val speedStr = when {
            speed < 0.85f -> "slow, deliberate speaking pace (${speed}x speed)"
            speed > 1.15f -> "brisk, fast speaking pace (${speed}x speed)"
            else -> "natural, steady pace (${speed}x speed)"
        }
        return "Pacing: $speedStr. Pitch register: ${pitch.label}. Energy projection: ${energy.label}. Pause style: ${pausing.label}."
    }
}
