package com.example.data.model

enum class Emotion(val label: String, val promptDescriptor: String, val iconEmoji: String) {
    NEUTRAL("Neutral", "balanced, objective, and neutral tone", "🎙️"),
    HAPPY("Happy", "joyful, cheerful, and smiling voice", "😊"),
    SAD("Sad", "somber, melancholy, and sorrowful tone", "😔"),
    ANGRY("Angry", "intense, stern, and fierce inflection", "😠"),
    EXCITED("Excited", "thrilled, enthusiastic, and high-energy delivery", "🤩"),
    CALM("Calm", "peaceful, serene, and relaxing cadence", "🌿"),
    CONFIDENT("Confident", "assured, resolute, and authoritative tone", "💪"),
    SERIOUS("Serious", "grave, earnest, and deeply solemn inflection", "🏛️"),
    INSPIRATIONAL("Inspirational", "uplifting, hopeful, and transcendent delivery", "✨"),
    MOTIVATIONAL("Motivational", "commanding, passionate, and action-driving tone", "🔥"),
    EMOTIONAL("Emotional", "deeply moving, vulnerable, and heartfelt inflection", "❤️"),
    DRAMATIC("Dramatic", "theatrical, tense, and climactic delivery", "🎭"),
    MYSTERIOUS("Mysterious", "enigmatic, hushed, and secretive cadence", "🕵️"),
    FRIENDLY("Friendly", "warm, welcoming, and approachable tone", "🤝"),
    WARM("Warm", "gentle, compassionate, and comforting voice", "☀️"),
    AUTHORITATIVE("Authoritative", "commanding, expert, and decisive delivery", "⚖️"),
    ENERGETIC("Energetic", "vibrant, dynamic, and fast-paced inflection", "⚡"),
    GENTLE("Gentle", "soft, tender, and soothing voice", "🕊️"),
    URGENT("Urgent", "rapid, high-stakes, and pressing delivery", "🚨"),
    HOPEFUL("Hopeful", "optimistic, radiant, and inspiring inflection", "🌅");

    companion object {
        fun fromLabel(label: String): Emotion = values().firstOrNull { it.label.equals(label, ignoreCase = true) } ?: NEUTRAL
    }
}

data class EmotionBlend(
    val primaryEmotion: Emotion = Emotion.NEUTRAL,
    val primaryPercentage: Int = 100,
    val secondaryEmotion: Emotion = Emotion.CALM,
    val secondaryPercentage: Int = 0,
    val intensity: Int = 75
) {
    fun toPromptDirection(): String {
        if (primaryEmotion == Emotion.NEUTRAL && secondaryPercentage == 0) {
            return "Deliver in a clean, natural, and balanced tone."
        }
        return if (secondaryPercentage > 0 && secondaryEmotion != primaryEmotion) {
            "Infuse emotional blend: ${primaryPercentage}% ${primaryEmotion.promptDescriptor} blended with ${secondaryPercentage}% ${secondaryEmotion.promptDescriptor}, at ${intensity}% intensity."
        } else {
            "Infuse ${intensity}% intensity of ${primaryEmotion.promptDescriptor}."
        }
    }
}
