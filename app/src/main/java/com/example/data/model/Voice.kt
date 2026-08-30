package com.example.data.model

enum class VoiceCategory(val label: String) {
    WARM("Warm"),
    FRIENDLY("Friendly"),
    PROFESSIONAL("Professional"),
    INFORMATIVE("Informative"),
    SMOOTH("Smooth"),
    FIRM("Firm"),
    MATURE("Mature"),
    YOUTHFUL("Youthful"),
    GENTLE("Gentle"),
    ENERGETIC("Energetic"),
    BRIGHT("Bright"),
    CASUAL("Casual"),
    AUTHORITATIVE("Authoritative")
}

data class GeminiVoice(
    val name: String,
    val characterTitle: String,
    val gender: String,
    val category: VoiceCategory,
    val description: String,
    val recommendedStyles: List<String>,
    val supportedLanguages: List<Language> = Language.ALL_SUPPORTED_LANGUAGES,
    val previewPitchHz: Float = 220f,
    val isFavorite: Boolean = false
) {
    companion object {
        // The 30 official Google Gemini TTS prebuilt voices
        val ALL_VOICES = listOf(
            GeminiVoice(
                name = "Puck",
                characterTitle = "Playful & Vibrant Narrator",
                gender = "Male",
                category = VoiceCategory.ENERGETIC,
                description = "High-energy, charismatic delivery ideal for entertainment, social media, and dynamic commercials.",
                recommendedStyles = listOf("YouTube", "Reels", "Entertainment", "Comedy", "Cartoon-style delivery"),
                previewPitchHz = 240f
            ),
            GeminiVoice(
                name = "Charon",
                characterTitle = "Deep & Gravitas Anchor",
                gender = "Male",
                category = VoiceCategory.AUTHORITATIVE,
                description = "Deep, resonant, cinematic baritone suitable for epic trailers, dark thrillers, and historical documentaries.",
                recommendedStyles = listOf("Cinematic", "Historical Documentary", "Movie Trailer", "Serious News"),
                previewPitchHz = 110f
            ),
            GeminiVoice(
                name = "Kore",
                characterTitle = "Warm & Natural Storyteller",
                gender = "Female",
                category = VoiceCategory.WARM,
                description = "Gentle, compassionate, and articulate tone perfect for audiobooks, meditation, and emotional storytelling.",
                recommendedStyles = listOf("Audiobook", "Emotional Speech", "Children's Story", "Calm", "Compassionate"),
                previewPitchHz = 260f
            ),
            GeminiVoice(
                name = "Fenrir",
                characterTitle = "Intense & Powerful Voice",
                gender = "Male",
                category = VoiceCategory.FIRM,
                description = "Bold, assertive voice with strong punch, ideal for motivational talks, action trailers, and executive presentations.",
                recommendedStyles = listOf("Motivational Speech", "Keynote", "Action", "Epic Trailer", "Corporate"),
                previewPitchHz = 130f
            ),
            GeminiVoice(
                name = "Aoede",
                characterTitle = "Silky & Elegant Host",
                gender = "Female",
                category = VoiceCategory.SMOOTH,
                description = "Luxurious, polished, and sophisticated cadence crafted for luxury brand advertisements and premium podcasts.",
                recommendedStyles = listOf("Luxury Advertisement", "Friendly Podcast", "Interview", "Travel Documentary"),
                previewPitchHz = 250f
            ),
            GeminiVoice(
                name = "Leda",
                characterTitle = "Clear & Academic Educator",
                gender = "Female",
                category = VoiceCategory.INFORMATIVE,
                description = "Clear diction, measured pacing, and friendly instructional voice for classroom lessons and explainer videos.",
                recommendedStyles = listOf("Teacher", "Classroom", "Lecture", "Tutorial", "Explainer"),
                previewPitchHz = 240f
            ),
            GeminiVoice(
                name = "Orus",
                characterTitle = "Trustworthy Corporate Executive",
                gender = "Male",
                category = VoiceCategory.PROFESSIONAL,
                description = "Polished, steady, and confidence-inspiring voice for financial reports, company overviews, and seminars.",
                recommendedStyles = listOf("Corporate", "Business", "Presentation", "Financial News", "Seminar"),
                previewPitchHz = 150f
            ),
            GeminiVoice(
                name = "Zephyr",
                characterTitle = "Breezy & Conversational Creator",
                gender = "Female",
                category = VoiceCategory.CASUAL,
                description = "Approachable, conversational, upbeat style suited for vlogs, social feeds, and modern lifestyle content.",
                recommendedStyles = listOf("Shorts", "TikTok", "Conversational", "Influencer", "Promotional"),
                previewPitchHz = 280f
            ),
            GeminiVoice(
                name = "Callisto",
                characterTitle = "Poised & Serious Journalist",
                gender = "Female",
                category = VoiceCategory.AUTHORITATIVE,
                description = "Formal, balanced journalistic cadence designed for breaking news, investigative journalism, and analysis.",
                recommendedStyles = listOf("News Anchor", "Breaking News", "Investigative Documentary", "News Report"),
                previewPitchHz = 220f
            ),
            GeminiVoice(
                name = "Autonoe",
                characterTitle = "Gentle & Empathetic Guide",
                gender = "Female",
                category = VoiceCategory.GENTLE,
                description = "Soft, soothing, and empathetic tone created for healthcare, emotional reassurance, and guided wellness.",
                recommendedStyles = listOf("Sad", "Hopeful", "Calm", "Compassionate", "Nature Documentary"),
                previewPitchHz = 230f
            ),
            GeminiVoice(
                name = "Enceladus",
                characterTitle = "Robust & Energetic Promoter",
                gender = "Male",
                category = VoiceCategory.ENERGETIC,
                description = "Punchy, fast-paced voice built for high-conversion sales ads, sports highlights, and dynamic announcements.",
                recommendedStyles = listOf("Commercial", "Product Advertisement", "Sales", "Energetic Advertisement"),
                previewPitchHz = 160f
            ),
            GeminiVoice(
                name = "Europa",
                characterTitle = "Refined & Cultured Narrator",
                gender = "Female",
                category = VoiceCategory.MATURE,
                description = "Cultured, articulate, and distinguished tone for museum audio guides, historical documentaries, and literature.",
                recommendedStyles = listOf("Historical Documentary", "Audiobook", "Documentary News", "Academic"),
                previewPitchHz = 210f
            ),
            GeminiVoice(
                name = "Ganymede",
                characterTitle = "Wise & Inspiring Mentor",
                gender = "Male",
                category = VoiceCategory.MATURE,
                description = "Warm, seasoned, and reflective delivery suitable for wisdom-driven essays, biopics, and leadership summits.",
                recommendedStyles = listOf("Inspirational", "Motivational", "Formal Speech", "Conference"),
                previewPitchHz = 140f
            ),
            GeminiVoice(
                name = "Iapetus",
                characterTitle = "Documentary & Nature Specialist",
                gender = "Male",
                category = VoiceCategory.INFORMATIVE,
                description = "Measured, awe-inspiring, and evocative narration crafted for wildlife, science, and cosmos documentaries.",
                recommendedStyles = listOf("Nature Documentary", "Scientific Documentary", "Cosmos", "Cinematic Documentary"),
                previewPitchHz = 135f
            ),
            GeminiVoice(
                name = "Io",
                characterTitle = "Bright & Youthful Presenter",
                gender = "Female",
                category = VoiceCategory.YOUTHFUL,
                description = "Lively, sparkling, and enthusiastic persona ideal for children's learning, gaming recaps, and interactive apps.",
                recommendedStyles = listOf("Children's Story", "Viral Explainer", "Comedy", "TikTok", "Tutorial"),
                previewPitchHz = 300f
            ),
            GeminiVoice(
                name = "Oberon",
                characterTitle = "Dramatic Theatrical Narrator",
                gender = "Male",
                category = VoiceCategory.AUTHORITATIVE,
                description = "Theatrical, commanding timbre with rich inflection for fantasy sagas, dramatic storytelling, and audio dramas.",
                recommendedStyles = listOf("Fantasy", "Adventure", "Dramatic Story", "Thriller", "Suspense"),
                previewPitchHz = 125f
            ),
            GeminiVoice(
                name = "Pegasus",
                characterTitle = "Dynamic Tech & Sci-Fi Voice",
                gender = "Male",
                category = VoiceCategory.BRIGHT,
                description = "Crisp, modern, and forward-looking acoustic profile suited for tech keynote recaps and futuristic trailers.",
                recommendedStyles = listOf("Scientific Documentary", "Technology", "Training", "Explainer"),
                previewPitchHz = 175f
            ),
            GeminiVoice(
                name = "Perseus",
                characterTitle = "Bold & Confident Pitchman",
                gender = "Male",
                category = VoiceCategory.FIRM,
                description = "Direct, engaging, and persuasive tone designed for radio commercials, product drops, and startup pitches.",
                recommendedStyles = listOf("Radio Commercial", "TV Commercial", "Company Introduction", "Sales"),
                previewPitchHz = 155f
            ),
            GeminiVoice(
                name = "Phoebe",
                characterTitle = "Warm & Welcoming Podcaster",
                gender = "Female",
                category = VoiceCategory.FRIENDLY,
                description = "Friendly, authentic, and naturally flowing conversational voice for roundtables, interviews, and deep chats.",
                recommendedStyles = listOf("Conversational", "Friendly Podcast", "Professional Podcast", "Interview"),
                previewPitchHz = 245f
            ),
            GeminiVoice(
                name = "Titan",
                characterTitle = "Commanding & Monumental Voice",
                gender = "Male",
                category = VoiceCategory.AUTHORITATIVE,
                description = "Thunderous, weighty low-end frequencies that give unmatched authority to monumental trailers and history specials.",
                recommendedStyles = listOf("Dark Narration", "Epic", "Cinematic", "Serious Documentary"),
                previewPitchHz = 95f
            ),
            GeminiVoice(
                name = "Umbriel",
                characterTitle = "Mysterious & Suspenseful Storyteller",
                gender = "Male",
                category = VoiceCategory.SMOOTH,
                description = "Whispered, suspenseful, and intriguing delivery for true crime podcasts, mystery thrillers, and detective tales.",
                recommendedStyles = listOf("Mystery", "Suspense", "Thriller", "Dark Narration"),
                previewPitchHz = 145f
            ),
            GeminiVoice(
                name = "Pandora",
                characterTitle = "Expressive & Versatile Performer",
                gender = "Female",
                category = VoiceCategory.ENERGETIC,
                description = "Highly dynamic modulation with wide emotional range, adaptable across varied commercial and story genres.",
                recommendedStyles = listOf("Character", "Dramatic", "Promotional", "Commercial"),
                previewPitchHz = 270f
            ),
            GeminiVoice(
                name = "Castor",
                characterTitle = "Calm & Reassuring Advisor",
                gender = "Male",
                category = VoiceCategory.WARM,
                description = "Warm, balanced, and reassuring presence for customer guidance, onboarding, and empathetic narratives.",
                recommendedStyles = listOf("Calm", "E-learning", "Tutorial", "Presentation"),
                previewPitchHz = 165f
            ),
            GeminiVoice(
                name = "Pollux",
                characterTitle = "Sharp & Engaging Host",
                gender = "Male",
                category = VoiceCategory.FRIENDLY,
                description = "Articulate, quick-witted, and friendly rhythm tailored for tech tutorials, news roundups, and co-hosting.",
                recommendedStyles = listOf("News Podcast", "Tutorial", "Explainer", "YouTube"),
                previewPitchHz = 180f
            ),
            GeminiVoice(
                name = "Vega",
                characterTitle = "Crisp & Modern Broadcaster",
                gender = "Female",
                category = VoiceCategory.PROFESSIONAL,
                description = "Pristine clarity, modern inflection, and immaculate tempo for international news, corporate briefings, and ads.",
                recommendedStyles = listOf("News Anchor", "Corporate", "Commercial", "Keynote"),
                previewPitchHz = 235f
            ),
            GeminiVoice(
                name = "Atlas",
                characterTitle = "Steadfast & Trustworthy Voice",
                gender = "Male",
                category = VoiceCategory.FIRM,
                description = "Unwavering, grounded, and dependable tone for educational curricula, security briefs, and non-fiction.",
                recommendedStyles = listOf("Documentary", "Educational", "Academic", "Formal Speech"),
                previewPitchHz = 138f
            ),
            GeminiVoice(
                name = "Sirius",
                characterTitle = "Bright & Uplifting Motivator",
                gender = "Male",
                category = VoiceCategory.BRIGHT,
                description = "Positive, forward-moving cadence crafted to inspire teams, launch campaigns, and energize listeners.",
                recommendedStyles = listOf("Inspirational Trailer", "Motivational Speech", "Promotional"),
                previewPitchHz = 190f
            ),
            GeminiVoice(
                name = "Polaris",
                characterTitle = "Clear & Neutral Benchmark",
                gender = "Female",
                category = VoiceCategory.INFORMATIVE,
                description = "Ultra-neutral, clear, and universally intelligible delivery across multi-language projects and international feeds.",
                recommendedStyles = listOf("Explainer", "Tutorial", "Lecture", "E-learning"),
                previewPitchHz = 225f
            ),
            GeminiVoice(
                name = "Lyra",
                characterTitle = "Melodic & Emotional Narrator",
                gender = "Female",
                category = VoiceCategory.WARM,
                description = "Poetic, tender, and deeply emotive voice for heartfelt tributes, emotional dramas, and audio poetry.",
                recommendedStyles = listOf("Emotional Speech", "Sad", "Hopeful", "Audiobook", "Children's Story"),
                previewPitchHz = 255f
            ),
            GeminiVoice(
                name = "Cressida",
                characterTitle = "Sparkling & Contemporary Storyteller",
                gender = "Female",
                category = VoiceCategory.YOUTHFUL,
                description = "Trendy, fresh, and engaging style for modern fiction, Gen-Z marketing, and interactive storytelling.",
                recommendedStyles = listOf("Adventure", "Social Media", "Shorts", "Conversational"),
                previewPitchHz = 290f
            )
        )

        fun findByName(name: String): GeminiVoice = ALL_VOICES.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: ALL_VOICES[0]
    }
}
