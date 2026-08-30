package com.example.data.model

enum class MusicCategory(val label: String) {
    CINEMATIC("Cinematic"),
    DOCUMENTARY("Documentary"),
    EMOTIONAL("Emotional"),
    INSPIRATIONAL("Inspirational"),
    CORPORATE("Corporate"),
    NEWS("News"),
    TECHNOLOGY("Technology"),
    AMBIENT("Ambient"),
    DRAMATIC("Dramatic"),
    SUSPENSE("Suspense"),
    CALM("Calm"),
    PIANO("Piano"),
    ACOUSTIC("Acoustic"),
    ELECTRONIC("Electronic"),
    EPIC("Epic"),
    PODCAST("Podcast")
}

data class MusicTrack(
    val id: String,
    val title: String,
    val category: MusicCategory,
    val durationSeconds: Int,
    val bpm: Int,
    val description: String,
    val baseFrequencyHz: Float,
    val chordProgression: List<Float> = listOf(220f, 261.63f, 329.63f, 392.0f),
    val isUserUploaded: Boolean = false,
    val localUri: String? = null,
    val isFavorite: Boolean = false
) {
    companion object {
        val NONE = MusicTrack(
            id = "none",
            title = "No Background Music",
            category = MusicCategory.CALM,
            durationSeconds = 0,
            bpm = 0,
            description = "Clean voice-only audio without musical accompaniment.",
            baseFrequencyHz = 0f
        )

        val BUILT_IN_TRACKS = listOf(
            NONE,
            MusicTrack(
                id = "cinematic_ambient",
                title = "Cinematic Ambient Horizon",
                category = MusicCategory.CINEMATIC,
                durationSeconds = 180,
                bpm = 75,
                description = "Lush synth pads and deep sub-bass creating an expansive cinematic atmosphere.",
                baseFrequencyHz = 110f,
                chordProgression = listOf(110f, 130.81f, 164.81f, 196f)
            ),
            MusicTrack(
                id = "documentary_deep",
                title = "Deep Earth Discovery",
                category = MusicCategory.DOCUMENTARY,
                durationSeconds = 210,
                bpm = 80,
                description = "Subtle acoustic pulses, marimba notes, and airy drones for wildlife and history.",
                baseFrequencyHz = 146.83f,
                chordProgression = listOf(146.83f, 174.61f, 220f, 261.63f)
            ),
            MusicTrack(
                id = "emotional_piano",
                title = "Memories & Rain (Soft Piano)",
                category = MusicCategory.PIANO,
                durationSeconds = 160,
                bpm = 68,
                description = "Gentle, heartfelt acoustic grand piano chords for tender and sorrowful moments.",
                baseFrequencyHz = 174.61f,
                chordProgression = listOf(174.61f, 220f, 261.63f, 329.63f)
            ),
            MusicTrack(
                id = "inspirational_rise",
                title = "Limitless Potential (Orchestral)",
                category = MusicCategory.INSPIRATIONAL,
                durationSeconds = 190,
                bpm = 96,
                description = "Rising strings, brass swells, and soaring harmonies that elevate human triumph.",
                baseFrequencyHz = 130.81f,
                chordProgression = listOf(130.81f, 164.81f, 196f, 246.94f)
            ),
            MusicTrack(
                id = "corporate_uplift",
                title = "Forward Momentum (Corporate)",
                category = MusicCategory.CORPORATE,
                durationSeconds = 140,
                bpm = 112,
                description = "Clean muted electric guitar, warm synth bass, and modern corporate optimism.",
                baseFrequencyHz = 196f,
                chordProgression = listOf(196f, 246.94f, 293.66f, 369.99f)
            ),
            MusicTrack(
                id = "news_bed_subtle",
                title = "Global Wire (News Bed)",
                category = MusicCategory.NEWS,
                durationSeconds = 120,
                bpm = 105,
                description = "Tight electronic pulse, ticking rhythm, and subtle synth arpeggios for news broadcasts.",
                baseFrequencyHz = 164.81f,
                chordProgression = listOf(164.81f, 196f, 246.94f, 293.66f)
            ),
            MusicTrack(
                id = "tech_future",
                title = "Neural Grid (Technology)",
                category = MusicCategory.TECHNOLOGY,
                durationSeconds = 175,
                bpm = 118,
                description = "Futuristic glitch beats, clean sine melodies, and high-tech electronic textures.",
                baseFrequencyHz = 220f,
                chordProgression = listOf(220f, 277.18f, 329.63f, 415.30f)
            ),
            MusicTrack(
                id = "podcast_chill",
                title = "Coffeehouse Lounge (Podcast)",
                category = MusicCategory.PODCAST,
                durationSeconds = 200,
                bpm = 88,
                description = "Warm Rhodes electric piano, mellow upright bass, and relaxed lo-fi groove.",
                baseFrequencyHz = 130.81f,
                chordProgression = listOf(130.81f, 155.56f, 196f, 233.08f)
            ),
            MusicTrack(
                id = "suspense_pulse",
                title = "Shadow In The Dark (Suspense)",
                category = MusicCategory.SUSPENSE,
                durationSeconds = 150,
                bpm = 70,
                description = "Heartbeat sub-kick, eerie dissonant strings, and ticking tension for mysteries.",
                baseFrequencyHz = 98f,
                chordProgression = listOf(98f, 103.83f, 146.83f, 155.56f)
            ),
            MusicTrack(
                id = "epic_trailer",
                title = "Warriors of the Dawn (Epic)",
                category = MusicCategory.EPIC,
                durationSeconds = 165,
                bpm = 124,
                description = "Thunderous cinematic percussion, choir stacks, and massive orchestral brass.",
                baseFrequencyHz = 110f,
                chordProgression = listOf(110f, 138.59f, 164.81f, 207.65f)
            ),
            MusicTrack(
                id = "calm_meditation",
                title = "Still Waters (Ambient Calm)",
                category = MusicCategory.CALM,
                durationSeconds = 240,
                bpm = 60,
                description = "Tibetan singing bowl resonance, soothing water stream, and 432Hz ambient drone.",
                baseFrequencyHz = 216f,
                chordProgression = listOf(216f, 270f, 324f, 432f)
            ),
            MusicTrack(
                id = "acoustic_breeze",
                title = "Sunlit Valley (Acoustic)",
                category = MusicCategory.ACOUSTIC,
                durationSeconds = 180,
                bpm = 92,
                description = "Fingerpicked nylon acoustic guitar, soft shaker, and rustic warm harmony.",
                baseFrequencyHz = 196f,
                chordProgression = listOf(196f, 246.94f, 293.66f, 392f)
            )
        )

        fun findById(id: String): MusicTrack = BUILT_IN_TRACKS.firstOrNull { it.id == id } ?: NONE
    }
}
