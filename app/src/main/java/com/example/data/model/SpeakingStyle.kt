package com.example.data.model

enum class StyleCategory(val label: String) {
    ADVERTISEMENT("Advertisement"),
    DOCUMENTARY("Documentary"),
    EDUCATION("Education"),
    NEWS("News"),
    EMOTIONAL("Emotional"),
    SOCIAL_MEDIA("Social Media"),
    CORPORATE("Corporate"),
    STORYTELLING("Storytelling"),
    PODCAST("Podcast"),
    CINEMATIC("Cinematic"),
    PUBLIC_SPEAKING("Public Speaking"),
    ENTERTAINMENT("Entertainment")
}

data class SpeakingStyle(
    val category: StyleCategory,
    val name: String,
    val description: String,
    val promptDirection: String
) {
    companion object {
        val ALL_STYLES = listOf(
            // ADVERTISEMENT
            SpeakingStyle(StyleCategory.ADVERTISEMENT, "Commercial", "General high-conversion commercial delivery", "Deliver with confident, engaging commercial punch, polished pacing, and clear call-to-action tone."),
            SpeakingStyle(StyleCategory.ADVERTISEMENT, "Product Advertisement", "Clear, feature-focused promotional voice", "Speak with bright enthusiasm, highlighting product benefits with crisp cadence."),
            SpeakingStyle(StyleCategory.ADVERTISEMENT, "TV Commercial", "Punchy broadcast-ready cadence", "Deliver in a broadcast-ready TV advertising cadence with dynamic pitch and energy."),
            SpeakingStyle(StyleCategory.ADVERTISEMENT, "Radio Commercial", "Fast, high-clarity audio ad delivery", "Fast-paced, vibrant, and razor-sharp radio announcement tempo."),
            SpeakingStyle(StyleCategory.ADVERTISEMENT, "Luxury Advertisement", "Slow, breathy, prestigious tone", "Speak in an elegant, hushed, ultra-luxurious, and sophisticated tone with subtle pauses."),
            SpeakingStyle(StyleCategory.ADVERTISEMENT, "Promotional", "Excited sales announcement", "Deliver with infectious excitement, urgency, and promotional vitality."),
            SpeakingStyle(StyleCategory.ADVERTISEMENT, "Sales", "Direct, persuasive pitch delivery", "Direct, persuasive, and convincing delivery with strong emphasis on key value propositions."),
            SpeakingStyle(StyleCategory.ADVERTISEMENT, "Energetic Advertisement", "High energy, fast hype delivery", "High-octane, upbeat, driving tempo that builds urgency."),
            SpeakingStyle(StyleCategory.ADVERTISEMENT, "Soft Advertisement", "Gentle, non-intrusive brand whisper", "Warm, gentle, and organic brand storytelling style."),

            // DOCUMENTARY
            SpeakingStyle(StyleCategory.DOCUMENTARY, "Documentary", "Standard balanced documentary narration", "Narrate in a calm, authoritative, informative, and cinematic documentary cadence."),
            SpeakingStyle(StyleCategory.DOCUMENTARY, "Historical Documentary", "Reflective, reverent historical voice", "Speak with gravitas, reverence, and historical weight, allowing room for reflection."),
            SpeakingStyle(StyleCategory.DOCUMENTARY, "Nature Documentary", "Awe-inspired, hushed wildlife narration", "Deliver in a hushed, awe-inspired, gentle wildlife narration tempo with dramatic natural pauses."),
            SpeakingStyle(StyleCategory.DOCUMENTARY, "Scientific Documentary", "Analytical, precise scientific explanation", "Clear, intellectual, and analytical delivery with pristine diction."),
            SpeakingStyle(StyleCategory.DOCUMENTARY, "Investigative Documentary", "Tense, investigative journalism tone", "Deliver with steady, serious, and inquisitive intrigue."),
            SpeakingStyle(StyleCategory.DOCUMENTARY, "Travel Documentary", "Vibrant, scenic cultural storytelling", "Warm, scenic, and inviting travel guide cadence."),
            SpeakingStyle(StyleCategory.DOCUMENTARY, "Serious Documentary", "Weighty, solemn documentary narration", "Deep, serious, and respectful cadence for solemn subjects."),
            SpeakingStyle(StyleCategory.DOCUMENTARY, "Cinematic Documentary", "Grand, epic widescreen documentary style", "Epic, sweeping, and cinematic storytelling cadence with resonant cadence."),

            // EDUCATION
            SpeakingStyle(StyleCategory.EDUCATION, "Teacher", "Patient, instructional classroom voice", "Warm, encouraging, and patient tone suitable for guided learning."),
            SpeakingStyle(StyleCategory.EDUCATION, "Classroom", "Engaging group lecture style", "Energetic, clear, and interactive instructional cadence."),
            SpeakingStyle(StyleCategory.EDUCATION, "Lecture", "Structured academic presentation", "Formal, intellectual, and clearly paced academic delivery."),
            SpeakingStyle(StyleCategory.EDUCATION, "Tutorial", "Step-by-step clear guidance", "Measured, crisp, and step-by-step instructional guide."),
            SpeakingStyle(StyleCategory.EDUCATION, "Educational", "General learning content voice", "Engaging, informative, and accessible educational tone."),
            SpeakingStyle(StyleCategory.EDUCATION, "Explainer", "Friendly concepts demystifier", "Friendly, relatable, and simplified concept delivery."),
            SpeakingStyle(StyleCategory.EDUCATION, "Academic", "Scholarly research delivery", "Sophisticated, scholarly, and articulate research presentation."),
            SpeakingStyle(StyleCategory.EDUCATION, "E-learning", "Modular, clear digital course voice", "Steady, pleasant, and easy-to-follow online course narration."),

            // NEWS
            SpeakingStyle(StyleCategory.NEWS, "News Anchor", "Standard television news anchor", "Crisp, neutral, authoritative television news anchor delivery."),
            SpeakingStyle(StyleCategory.NEWS, "Breaking News", "Urgent, fast-paced bulletin", "Urgent, fast, high-focus breaking news alert cadence."),
            SpeakingStyle(StyleCategory.NEWS, "News Report", "On-the-scene field report", "Direct, objective, and realistic journalistic field reporting tone."),
            SpeakingStyle(StyleCategory.NEWS, "Serious News", "Solemn, formal news briefing", "Solemn, dignified, and formal news broadcast delivery."),
            SpeakingStyle(StyleCategory.NEWS, "Financial News", "Data-driven market report", "Crisp, concise, and professional financial market analysis cadence."),
            SpeakingStyle(StyleCategory.NEWS, "Documentary News", "In-depth current affairs analysis", "Thoughtful, investigative, and deep current affairs narration."),

            // EMOTIONAL
            SpeakingStyle(StyleCategory.EMOTIONAL, "Emotional Speech", "Heartfelt, deeply moving delivery", "Deliver with deep emotional resonance, subtle vulnerability, and heartfelt inflection."),
            SpeakingStyle(StyleCategory.EMOTIONAL, "Sad", "Somber, grieving, tearful delivery", "Somber, quiet, sorrowful tone with trembling, slow cadence."),
            SpeakingStyle(StyleCategory.EMOTIONAL, "Hopeful", "Uplifting, optimistic dawn voice", "Tender, uplifting, and warmly optimistic inflection."),
            SpeakingStyle(StyleCategory.EMOTIONAL, "Inspirational", "Empowering, transformative cadence", "Resonant, uplifting, and empowering delivery that moves the spirit."),
            SpeakingStyle(StyleCategory.EMOTIONAL, "Motivational", "Driving, high-willpower speech", "Passionate, resolute, and commanding motivational speech delivery."),
            SpeakingStyle(StyleCategory.EMOTIONAL, "Dramatic", "Theatrical, high-stakes emotional arc", "Theatrical, high-tension dramatic delivery with shifting volume."),
            SpeakingStyle(StyleCategory.EMOTIONAL, "Powerful", "Resonant, impactful declaration", "Commanding, thunderous, and unforgettable declaration."),
            SpeakingStyle(StyleCategory.EMOTIONAL, "Serious", "Uncompromising, stern gravity", "Stern, unyielding, and serious inflection."),
            SpeakingStyle(StyleCategory.EMOTIONAL, "Calm", "Peaceful, meditative tranquility", "Smooth, soothing, and tranquil breath-filled delivery."),
            SpeakingStyle(StyleCategory.EMOTIONAL, "Compassionate", "Warm, caring empathy", "Deeply caring, gentle, and empathetic tone."),

            // SOCIAL MEDIA
            SpeakingStyle(StyleCategory.SOCIAL_MEDIA, "YouTube", "Engaging creator intro and flow", "Dynamic, energetic, approachable YouTube creator pacing."),
            SpeakingStyle(StyleCategory.SOCIAL_MEDIA, "Shorts", "Fast hook and retention delivery", "Punchy, fast, and hook-driven delivery crafted for 60-second shorts."),
            SpeakingStyle(StyleCategory.SOCIAL_MEDIA, "Reels", "Trendy, rhythmic social delivery", "Modern, rhythmic, and trend-focused social media cadence."),
            SpeakingStyle(StyleCategory.SOCIAL_MEDIA, "TikTok", "Casual, ultra-relatable viral tone", "Casual, authentic, fast-paced viral storytelling style."),
            SpeakingStyle(StyleCategory.SOCIAL_MEDIA, "Viral Explainer", "Curiosity-sparking narrative", "High-curiosity, fast-revealing viral explainer cadence."),
            SpeakingStyle(StyleCategory.SOCIAL_MEDIA, "Influencer", "Charismatic personal monologue", "Charismatic, conversational, and direct-to-audience influencer voice."),
            SpeakingStyle(StyleCategory.SOCIAL_MEDIA, "Conversational", "Friendly one-on-one chat", "Natural, informal, and relaxed conversation tempo."),

            // CORPORATE
            SpeakingStyle(StyleCategory.CORPORATE, "Corporate", "Standard executive enterprise tone", "Polished, professional, and confident corporate presentation voice."),
            SpeakingStyle(StyleCategory.CORPORATE, "Business", "Strategic business briefing", "Focused, decisive, and strategic business delivery."),
            SpeakingStyle(StyleCategory.CORPORATE, "Professional", "High-standard workplace narration", "Crisp, respectful, and articulate professional cadence."),
            SpeakingStyle(StyleCategory.CORPORATE, "Executive", "Boardroom leadership presence", "Authoritative, calm, and visionary leadership delivery."),
            SpeakingStyle(StyleCategory.CORPORATE, "Presentation", "Slide deck narration pacing", "Measured, engaging, and clear presentation walkthrough."),
            SpeakingStyle(StyleCategory.CORPORATE, "Training", "Employee onboarding guidance", "Supportive, clear, and structured employee training cadence."),
            SpeakingStyle(StyleCategory.CORPORATE, "Company Introduction", "Inspiring brand origin story", "Proud, visionary, and welcoming company introduction."),

            // STORYTELLING
            SpeakingStyle(StyleCategory.STORYTELLING, "Storytelling", "Classic immersive storyteller", "Immersive, expressive, and captivating narrative cadence."),
            SpeakingStyle(StyleCategory.STORYTELLING, "Audiobook", "Long-form audiobook narrator", "Comfortable, expressive, and sustained audiobook storytelling pace."),
            SpeakingStyle(StyleCategory.STORYTELLING, "Fantasy", "Epic magical world narration", "Grand, enchanted, and wondrous fantasy world delivery."),
            SpeakingStyle(StyleCategory.STORYTELLING, "Adventure", "Fast, action-packed escapade", "Thrilling, pulse-racing adventure narration."),
            SpeakingStyle(StyleCategory.STORYTELLING, "Mystery", "Enigmatic, riddle-filled cadence", "Whispered, suspenseful, and calculating mystery tone."),
            SpeakingStyle(StyleCategory.STORYTELLING, "Children's Story", "Playful, animated fairy tale", "Playful, animated, wide vocal range for fairy tales."),
            SpeakingStyle(StyleCategory.STORYTELLING, "Dramatic Story", "Intense literary drama", "Deeply nuanced, literary drama narration."),

            // PODCAST
            SpeakingStyle(StyleCategory.PODCAST, "Conversational", "Relaxed studio co-host chat", "Relaxed, genuine, and warm studio conversation."),
            SpeakingStyle(StyleCategory.PODCAST, "Friendly Podcast", "Charming host banter", "Warm, humorous, and charming podcast hosting delivery."),
            SpeakingStyle(StyleCategory.PODCAST, "Professional Podcast", "Thought-leadership interview", "Informed, respectful, and intellectually curious podcast pacing."),
            SpeakingStyle(StyleCategory.PODCAST, "Interview", "Dynamic Q&A back and forth", "Attentive, inquisitive, and engaging interview flow."),
            SpeakingStyle(StyleCategory.PODCAST, "News Podcast", "Daily news digest recap", "Concise, engaging daily news recap cadence."),

            // CINEMATIC
            SpeakingStyle(StyleCategory.CINEMATIC, "Movie Trailer", "Epic Hollywood blockbuster trailer", "Massive, booming, cinematic Hollywood blockbuster trailer voice."),
            SpeakingStyle(StyleCategory.CINEMATIC, "Cinematic", "Widescreen atmospheric narration", "Rich, atmospheric, and resonant cinematic delivery."),
            SpeakingStyle(StyleCategory.CINEMATIC, "Epic", "Heroic, mythological grandeur", "Heroic, boundless, and mythological resonance."),
            SpeakingStyle(StyleCategory.CINEMATIC, "Suspense", "Tense, breath-holding thriller", "Tense, whisper-quiet, heart-stopping suspense cadence."),
            SpeakingStyle(StyleCategory.CINEMATIC, "Thriller", "Dark psychological edge", "Dark, gripping, and psychologically sharp thriller tone."),
            SpeakingStyle(StyleCategory.CINEMATIC, "Dark Narration", "Noir, gritty brooding voice", "Gritty, brooding, noir-style baritone narration."),
            SpeakingStyle(StyleCategory.CINEMATIC, "Inspirational Trailer", "Building emotional climax", "Gradually building from gentle reflection to thunderous inspiration."),

            // PUBLIC SPEAKING
            SpeakingStyle(StyleCategory.PUBLIC_SPEAKING, "Formal Speech", "Dignified podium address", "Statesmanlike, stately, and formal public address."),
            SpeakingStyle(StyleCategory.PUBLIC_SPEAKING, "Conference", "Keynote hall address", "Dynamic, clear, and expansive conference hall delivery."),
            SpeakingStyle(StyleCategory.PUBLIC_SPEAKING, "Seminar", "Interactive educational seminar", "Engaging, well-paced seminar discussion."),
            SpeakingStyle(StyleCategory.PUBLIC_SPEAKING, "Keynote", "Visionary tech keynote address", "Visionary, confident, and inspiring keynote cadence."),
            SpeakingStyle(StyleCategory.PUBLIC_SPEAKING, "Motivational Speech", "Fired-up rally address", "High-intensity, passionate, rally-stage motivational delivery."),
            SpeakingStyle(StyleCategory.PUBLIC_SPEAKING, "Educational Speech", "Commencement and graduation", "Heartfelt, reflective, and empowering graduation speech."),

            // ENTERTAINMENT
            SpeakingStyle(StyleCategory.ENTERTAINMENT, "Character", "Animated distinct persona", "Colorful, exaggerated, and memorable character voice."),
            SpeakingStyle(StyleCategory.ENTERTAINMENT, "Comedy", "Punchy comedic timing", "Playful, witty, with precise comedic pauses and timing."),
            SpeakingStyle(StyleCategory.ENTERTAINMENT, "Energetic", "High-spirit party host", "High-energy, joyous, and festive entertainment host."),
            SpeakingStyle(StyleCategory.ENTERTAINMENT, "Dramatic", "Melodramatic soap opera delivery", "Intense, theatrical melodrama with heavy sighing and emphasis."),
            SpeakingStyle(StyleCategory.ENTERTAINMENT, "Suspense", "Spooky campfire tale", "Creepy, atmospheric, and spine-chilling ghost story cadence."),
            SpeakingStyle(StyleCategory.ENTERTAINMENT, "Cartoon-style delivery", "Bubbly cartoon mascot", "Animated, bright, bubbly cartoon mascot pacing.")
        )

        fun findByName(name: String): SpeakingStyle = ALL_STYLES.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: ALL_STYLES[0]
    }
}
