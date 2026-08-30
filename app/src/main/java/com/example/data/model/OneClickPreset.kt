package com.example.data.model

data class OneClickPreset(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val voiceName: String,
    val style: String,
    val primaryEmotion: Emotion,
    val secondaryEmotion: Emotion,
    val emotionIntensity: Int,
    val speed: Float,
    val pitch: PitchSetting,
    val musicTrackId: String,
    val autoDucking: Boolean = true
) {
    companion object {
        val ALL_PRESETS = listOf(
            OneClickPreset(
                id = "cinematic_documentary",
                title = "Cinematic Documentary",
                description = "Grand, immersive widescreen narration with serious & dramatic weight.",
                iconEmoji = "🎬",
                voiceName = "Charon",
                style = "Cinematic Documentary",
                primaryEmotion = Emotion.SERIOUS,
                secondaryEmotion = Emotion.DRAMATIC,
                emotionIntensity = 85,
                speed = 0.90f,
                pitch = PitchSetting.LOW,
                musicTrackId = "cinematic_ambient"
            ),
            OneClickPreset(
                id = "commercial_advertisement",
                title = "Commercial Advertisement",
                description = "High-impact, confident, and energetic sales & brand campaign voice.",
                iconEmoji = "📢",
                voiceName = "Enceladus",
                style = "Commercial",
                primaryEmotion = Emotion.CONFIDENT,
                secondaryEmotion = Emotion.ENERGETIC,
                emotionIntensity = 90,
                speed = 1.10f,
                pitch = PitchSetting.NORMAL,
                musicTrackId = "corporate_uplift"
            ),
            OneClickPreset(
                id = "emotional_speech",
                title = "Emotional Speech",
                description = "Heartfelt, tender, and deeply vulnerable storytelling.",
                iconEmoji = "❤️",
                voiceName = "Lyra",
                style = "Emotional Speech",
                primaryEmotion = Emotion.WARM,
                secondaryEmotion = Emotion.EMOTIONAL,
                emotionIntensity = 80,
                speed = 0.85f,
                pitch = PitchSetting.NORMAL,
                musicTrackId = "emotional_piano"
            ),
            OneClickPreset(
                id = "educational_lecture",
                title = "Educational Lecture",
                description = "Clear, patient, instructional, and structured teaching cadence.",
                iconEmoji = "🎓",
                voiceName = "Leda",
                style = "Educational",
                primaryEmotion = Emotion.CALM,
                secondaryEmotion = Emotion.FRIENDLY,
                emotionIntensity = 70,
                speed = 0.95f,
                pitch = PitchSetting.NORMAL,
                musicTrackId = "none"
            ),
            OneClickPreset(
                id = "news_broadcast",
                title = "News Broadcast",
                description = "Crisp, authoritative, neutral television news anchor delivery.",
                iconEmoji = "📰",
                voiceName = "Vega",
                style = "News Anchor",
                primaryEmotion = Emotion.SERIOUS,
                secondaryEmotion = Emotion.AUTHORITATIVE,
                emotionIntensity = 85,
                speed = 1.00f,
                pitch = PitchSetting.NORMAL,
                musicTrackId = "news_bed_subtle"
            ),
            OneClickPreset(
                id = "podcast_host",
                title = "Podcast Host",
                description = "Natural, conversational studio host banter with warm presence.",
                iconEmoji = "🎙️",
                voiceName = "Phoebe",
                style = "Conversational",
                primaryEmotion = Emotion.FRIENDLY,
                secondaryEmotion = Emotion.CALM,
                emotionIntensity = 75,
                speed = 1.00f,
                pitch = PitchSetting.NORMAL,
                musicTrackId = "podcast_chill"
            ),
            OneClickPreset(
                id = "documentary_standard",
                title = "Documentary",
                description = "Calm, deeply informative, and measured nature/history narrator.",
                iconEmoji = "🌍",
                voiceName = "Iapetus",
                style = "Documentary",
                primaryEmotion = Emotion.CALM,
                secondaryEmotion = Emotion.SERIOUS,
                emotionIntensity = 80,
                speed = 0.95f,
                pitch = PitchSetting.NORMAL,
                musicTrackId = "documentary_deep"
            )
        )
    }
}
