package com.example.data.model

data class SampleProject(
    val id: String,
    val title: String,
    val description: String,
    val language: Language,
    val voiceName: String,
    val style: String,
    val primaryEmotion: Emotion,
    val secondaryEmotion: Emotion,
    val emotionIntensity: Int,
    val speed: Float,
    val pitch: PitchSetting,
    val musicTrackId: String,
    val script: String,
    val scenes: List<SceneItem>
) {
    companion object {
        val ALL_SAMPLES = listOf(
            SampleProject(
                id = "sample_urdu_doc",
                title = "Urdu Historical Documentary — شاہراہِ ریشم کی داستان",
                description = "Deep, resonant Urdu historical documentary exploring the legendary ancient Silk Road and civilization.",
                language = Language.URDU,
                voiceName = "Charon",
                style = "Historical Documentary",
                primaryEmotion = Emotion.SERIOUS,
                secondaryEmotion = Emotion.DRAMATIC,
                emotionIntensity = 85,
                speed = 0.90f,
                pitch = PitchSetting.LOW,
                musicTrackId = "documentary_deep",
                script = "شاہراہِ ریشم، تاریخ کی وہ عظیم راہ گزر ہے جس نے صدیوں تک مشرق اور مغرب کی تہذیبوں کو ایک لڑی میں پروئے رکھا۔ [pause 1.0s] یہاں کاروانوں کے قدموں کے نشانات آج بھی وادیوں میں گونجتے ہیں، جہاں تاجروں نے نہ صرف ریشم اور مسالے بیچے، بلکہ فلسفہ اور علم کے چراغ بھی روشن کیے۔",
                scenes = listOf(
                    SceneItem(title = "منظر ۱ — تعارف و قدامت", text = "شاہراہِ ریشم، تاریخ کی وہ عظیم راہ گزر ہے جس نے صدیوں تک مشرق اور مغرب کو جوڑے رکھا۔", voiceName = "Charon", style = "Historical Documentary", emotion = "Serious", speed = 0.90f, pitch = "Low"),
                    SceneItem(title = "منظر ۲ — کاروان اور صحرا", text = "بلند وبالا پہاڑوں اور وسیع صحراؤں کے پار، قافلے علم اور حکمت کی روشنی پھیلایا کرتے تھے۔", voiceName = "Charon", style = "Historical Documentary", emotion = "Dramatic", speed = 0.90f, pitch = "Low"),
                    SceneItem(title = "منظر ۳ — تاریخ کا پیغام", text = "آج بھی ان کھنڈرات میں قدیم دانائی کی بازگشت سنی جا سکتی ہے۔", voiceName = "Charon", style = "Historical Documentary", emotion = "Calm", speed = 0.95f, pitch = "Normal")
                )
            ),
            SampleProject(
                id = "sample_urdu_emotional",
                title = "Urdu Emotional Speech — امید کا دیا",
                description = "Heartfelt, deeply moving Urdu narration about resilience, hope, and conquering inner darkness.",
                language = Language.URDU,
                voiceName = "Lyra",
                style = "Emotional Speech",
                primaryEmotion = Emotion.WARM,
                secondaryEmotion = Emotion.EMOTIONAL,
                emotionIntensity = 85,
                speed = 0.85f,
                pitch = PitchSetting.NORMAL,
                musicTrackId = "emotional_piano",
                script = "جب رات گہری ہو جائے اور ہر طرف مایوسی کے سائے منڈلانے لگیں، [pause 0.5s] تو یاد رکھیے کہ تاریکی کا مقدر صبح کے نور کے آگے مٹ جانا ہے۔ اپنے دل کے یقین کو کبھی مدہم نہ ہونے دیں۔ [pause 1.0s] کیونکہ ہر گرنے والا قطرہ ایک نئی بہار کا پیش خیمہ ہوتا ہے۔",
                scenes = listOf(
                    SceneItem(title = "Scene 1 — Darkness", text = "جب رات گہری ہو جائے اور ہر طرف مایوسی کے سائے منڈلانے لگیں...", voiceName = "Lyra", style = "Emotional Speech", emotion = "Sad", speed = 0.80f),
                    SceneItem(title = "Scene 2 — The Spark", text = "تو یاد رکھیے کہ تاریکی کا مقدر صبح کے نور کے آگے مٹ جانا ہے۔", voiceName = "Lyra", style = "Emotional Speech", emotion = "Warm", speed = 0.85f),
                    SceneItem(title = "Scene 3 — Hope", text = "اپنے دل کے یقین کو کبھی مدہم نہ ہونے دیں، نئی صبح منتظر ہے۔", voiceName = "Lyra", style = "Emotional Speech", emotion = "Hopeful", speed = 0.90f)
                )
            ),
            SampleProject(
                id = "sample_urdu_ad",
                title = "Urdu Tech Commercial — ووکسورا اسمارٹ کلاؤڈ",
                description = "High-energy commercial advertisement in Urdu for enterprise cloud AI innovations.",
                language = Language.URDU,
                voiceName = "Enceladus",
                style = "Commercial",
                primaryEmotion = Emotion.CONFIDENT,
                secondaryEmotion = Emotion.ENERGETIC,
                emotionIntensity = 90,
                speed = 1.10f,
                pitch = PitchSetting.NORMAL,
                musicTrackId = "corporate_uplift",
                script = "کیا آپ کا کاروبار تیز رفتار مستقبل کے لیے تیار ہے؟ [pause 0.5s] پیش ہے ووکسورا کلاؤڈ، جدید ترین مصنوعی ذہانت کی طاقت کے ساتھ! اب اپنے کاروبار کو دیں بے مثال رفتار اور محفوظ ترین کلاؤڈ انٹیگریشن۔ آج ہی آزمائیں!",
                scenes = listOf(
                    SceneItem(title = "Hook", text = "کیا آپ کا کاروبار تیز رفتار مستقبل کے لیے تیار ہے؟", voiceName = "Enceladus", style = "Commercial", emotion = "Confident", speed = 1.10f),
                    SceneItem(title = "Value Prop", text = "پیش ہے جدید ترین مصنوعی ذہانت کی کلاؤڈ طاقت جو آپ کے کام کو دے بے مثال رفتار۔", voiceName = "Enceladus", style = "Commercial", emotion = "Energetic", speed = 1.10f),
                    SceneItem(title = "CTA", text = "آج ہی ووکسورا سے جڑیں اور نئی بلندیوں کو چھوئیں۔", voiceName = "Enceladus", style = "Commercial", emotion = "Urgent", speed = 1.15f)
                )
            ),
            SampleProject(
                id = "sample_english_commercial",
                title = "English Commercial — Apex Velocity Pro",
                description = "Punchy, modern global brand advertisement with driving dynamic energy.",
                language = Language.ENGLISH,
                voiceName = "Perseus",
                style = "Commercial",
                primaryEmotion = Emotion.CONFIDENT,
                secondaryEmotion = Emotion.ENERGETIC,
                emotionIntensity = 90,
                speed = 1.10f,
                pitch = PitchSetting.NORMAL,
                musicTrackId = "corporate_uplift",
                script = "Speed isn't just a number—it's your ultimate competitive advantage. [pause 0.5s] Introducing Apex Velocity Pro. Engineered with quantum carbon-fiber agility and hyper-responsive precision. [pause 1.0s] Don't just keep up with the world. Lead it. Available now.",
                scenes = listOf(
                    SceneItem(title = "Hook", text = "Speed isn't just a number—it's your ultimate competitive advantage.", voiceName = "Perseus", style = "Commercial", emotion = "Confident", speed = 1.10f),
                    SceneItem(title = "Product Features", text = "Engineered with quantum carbon-fiber agility and hyper-responsive precision.", voiceName = "Perseus", style = "Product Advertisement", emotion = "Excited", speed = 1.10f),
                    SceneItem(title = "Call to Action", text = "Don't just keep up with the world. Lead it. Order yours today.", voiceName = "Perseus", style = "Commercial", emotion = "Energetic", speed = 1.15f)
                )
            ),
            SampleProject(
                id = "sample_english_doc",
                title = "English Cinematic Documentary — Deep Ocean Mysteries",
                description = "Awe-inspiring nature & exploration documentary narration about the deepest ocean trenches.",
                language = Language.ENGLISH,
                voiceName = "Iapetus",
                style = "Nature Documentary",
                primaryEmotion = Emotion.CALM,
                secondaryEmotion = Emotion.MYSTERIOUS,
                emotionIntensity = 85,
                speed = 0.90f,
                pitch = PitchSetting.LOW,
                musicTrackId = "cinematic_ambient",
                script = "Beneath the sunlit surface of our world lies an alien kingdom cloaked in perpetual midnight. [pause 1.0s] Six miles down, in the silent Mariana Trench, crushing pressures give birth to miraculous, bioluminescent lifeforms that defy our understanding of biology. [pause 1.5s] Welcome to the abyss.",
                scenes = listOf(
                    SceneItem(title = "Scene 1 — The Descent", text = "Beneath the sunlit surface lies an alien kingdom cloaked in perpetual midnight.", voiceName = "Iapetus", style = "Nature Documentary", emotion = "Mysterious", speed = 0.90f),
                    SceneItem(title = "Scene 2 — The Trench", text = "Six miles down, crushing pressures give birth to miraculous bioluminescent lifeforms.", voiceName = "Iapetus", style = "Scientific Documentary", emotion = "Calm", speed = 0.90f),
                    SceneItem(title = "Scene 3 — The Wonder", text = "In the darkest corners of Earth, life still finds an extraordinary way.", voiceName = "Iapetus", style = "Cinematic Documentary", emotion = "Dramatic", speed = 0.90f)
                )
            ),
            SampleProject(
                id = "sample_arabic_doc",
                title = "Arabic Documentary — أسرار الحضارة الأندلسية",
                description = "Grand, eloquent Modern Standard Arabic documentary exploring the golden age of Andalusian architecture and science.",
                language = Language.ARABIC,
                voiceName = "Charon",
                style = "Historical Documentary",
                primaryEmotion = Emotion.SERIOUS,
                secondaryEmotion = Emotion.AUTHORITATIVE,
                emotionIntensity = 85,
                speed = 0.95f,
                pitch = PitchSetting.LOW,
                musicTrackId = "documentary_deep",
                script = "في قلب الأندلس، تلألأت قرطبة كمنارة للعلم والمعرفة حين كان العالم غارقاً في الظلمات. [pause 1.0s] هنا التقت علوم الفلك والطب وفنون العمارة الخالدة، لتبني إرثاً إنسانياً شامخاً ما زالت جدران قصر الحمراء تروي أسراره حتى اليوم.",
                scenes = listOf(
                    SceneItem(title = "المشهد ۱ — المجد الأندلسي", text = "في قلب الأندلس، تلألأت قرطبة كمنارة للعلم والمعرفة حين كان العالم يبحث عن الضياء.", voiceName = "Charon", style = "Historical Documentary", emotion = "Serious", speed = 0.95f),
                    SceneItem(title = "المشهد ۲ — قصر الحمراء", text = "هنا التقت علوم الفلك والطب وفنون العمارة الخالدة، لتبني إرثاً إنسانياً لا يزول.", voiceName = "Charon", style = "Cinematic Documentary", emotion = "Dramatic", speed = 0.90f)
                )
            ),
            SampleProject(
                id = "sample_persian_story",
                title = "Persian Storytelling — راز باغستان کهن",
                description = "Poetic and enchanting Persian/Farsi storytelling with melodic cadence and lush imagery.",
                language = Language.PERSIAN,
                voiceName = "Kore",
                style = "Storytelling",
                primaryEmotion = Emotion.WARM,
                secondaryEmotion = Emotion.MYSTERIOUS,
                emotionIntensity = 80,
                speed = 0.90f,
                pitch = PitchSetting.NORMAL,
                musicTrackId = "emotional_piano",
                script = "در دامنه‌های البرز سر به فلک کشیده، باغستانی کهن نهفته بود که نسیم بهاری در میان درختان چنار آن زمزمه‌ای از رازهای باستان سر می‌داد. [pause 1.0s] پیر خردمند دهکده می‌گفت هر برگ این درختان، داستانی از عشق، وفاداری و گذر ایام است.",
                scenes = listOf(
                    SceneItem(title = "بخش نخست — باغ افسانه‌ای", text = "در دامنه‌های البرز سر به فلک کشیده، باغستانی کهن نهفته بود که نسیمش راز باستان داشت.", voiceName = "Kore", style = "Storytelling", emotion = "Warm", speed = 0.90f),
                    SceneItem(title = "بخش دوم — کلام پیر خرد", text = "پیر خردمند دهکده می‌گفت هر برگ این درختان، داستانی از عشق و گذر ایام است.", voiceName = "Kore", style = "Audiobook", emotion = "Calm", speed = 0.90f)
                )
            ),
            SampleProject(
                id = "sample_pashto_speech",
                title = "Pashto Speech — د بریا او ننګ لار",
                description = "Empowering, resolute, and dignified Pashto keynote speech on bravery, education, and unity.",
                language = Language.PASHTO,
                voiceName = "Fenrir",
                style = "Motivational Speech",
                primaryEmotion = Emotion.MOTIVATIONAL,
                secondaryEmotion = Emotion.CONFIDENT,
                emotionIntensity = 88,
                speed = 0.95f,
                pitch = PitchSetting.NORMAL,
                musicTrackId = "inspirational_rise",
                script = "د هر قوم او ملت راتلونکی د هغوی د ځوانانو په علم، همت او یووالي پورې تړلی دی. [pause 1.0s] کله چې اراده پخه وي او هڅې پرله پسې وي، نو هیڅ خنډ د پرمختګ مخه نشي نیولی. راځئ چې په پوهه او زړورتیا سره خپل وطن د هوساینې لوړو ته ورسوو.",
                scenes = listOf(
                    SceneItem(title = "لومړۍ برخه — همت او اراده", text = "د هر قوم او ملت راتلونکی د ځوانانو په علم او کلک هوډ پورې تړلی دی.", voiceName = "Fenrir", style = "Motivational Speech", emotion = "Motivational", speed = 0.95f),
                    SceneItem(title = "دوهمه برخه — د روښانه سباوون غږ", text = "راځئ چې په پوهه او یووالي سره د سبا ننګونې وګټو او خپل هدف ته ورسیږو.", voiceName = "Fenrir", style = "Formal Speech", emotion = "Confident", speed = 0.95f)
                )
            ),
            SampleProject(
                id = "sample_hindi_lecture",
                title = "Hindi Educational Lecture — ब्रह्मांड के रहस्य और अंतरिक्ष विज्ञान",
                description = "Clear, engaging, and academic Hindi lecture explaining astrophysics and space exploration.",
                language = Language.HINDI,
                voiceName = "Leda",
                style = "Lecture",
                primaryEmotion = Emotion.CALM,
                secondaryEmotion = Emotion.CONFIDENT,
                emotionIntensity = 75,
                speed = 0.95f,
                pitch = PitchSetting.NORMAL,
                musicTrackId = "none",
                script = "नमस्ते और खगोल भौतिकी के इस विशेष सत्र में आपका स्वागत है। [pause 0.8s] जब हम रात के आकाश की ओर देखते हैं, तो हम वास्तव में अरबों वर्ष पुराने प्रकाश को देख रहे होते हैं। आज हम समझेंगे कि कैसे गुरुत्वाकर्षण तारे बनाता है और कैसे ब्लैक होल समय के प्रवाह को मोड़ देते हैं।",
                scenes = listOf(
                    SceneItem(title = "भाग १ — भूमिका", text = "नमस्ते और खगोल भौतिकी के इस विशेष सत्र में आपका स्वागत है।", voiceName = "Leda", style = "Lecture", emotion = "Friendly", speed = 0.95f),
                    SceneItem(title = "भाग २ — तारों का निर्माण", text = "जब हम रात के आकाश को देखते हैं, तो हम करोड़ों वर्ष पुराने प्रकाश के साक्षी बनते हैं।", voiceName = "Leda", style = "Educational", emotion = "Calm", speed = 0.95f),
                    SceneItem(title = "भाग ३ — गुरुत्वाकर्षण और समय", text = "गुरुत्वाकर्षण केवल एक बल नहीं, बल्कि समय और दिक्-स्थान का सुंदर वक्र है।", voiceName = "Leda", style = "Scientific Documentary", emotion = "Informative", speed = 0.95f)
                )
            ),
            SampleProject(
                id = "sample_chinese_corporate",
                title = "Chinese Corporate Presentation — 智领未来：全球数字化转型战略",
                description = "Polished, executive Mandarin Chinese presentation for international business summits.",
                language = Language.CHINESE,
                voiceName = "Vega",
                style = "Executive",
                primaryEmotion = Emotion.CONFIDENT,
                secondaryEmotion = Emotion.AUTHORITATIVE,
                emotionIntensity = 85,
                speed = 1.00f,
                pitch = PitchSetting.NORMAL,
                musicTrackId = "corporate_uplift",
                script = "各位嘉宾，欢迎来到2026全球数字峰会。 [pause 0.8s] 在当今瞬息万变的全球化市场中，智能化与算力升级已经成为企业跨越式发展的核心引擎。 [pause 1.0s] 我们的愿景是以安全、高效的AI生态，赋能每一个合作伙伴开创数字化新纪元。",
                scenes = listOf(
                    SceneItem(title = "开场致辞", text = "各位嘉宾，欢迎来到2026全球数字峰会。", voiceName = "Vega", style = "Executive", emotion = "Confident", speed = 1.00f),
                    SceneItem(title = "核心战略", text = "智能化与算力升级已成为企业跨越式发展的核心引擎。", voiceName = "Vega", style = "Corporate", emotion = "Authoritative", speed = 1.00f),
                    SceneItem(title = "合作愿景", text = "让我们携手并进，共同开创繁荣共赢的数字化新纪元。", voiceName = "Vega", style = "Keynote", emotion = "Inspirational", speed = 1.00f)
                )
            )
        )

        fun findById(id: String): SampleProject = ALL_SAMPLES.firstOrNull { it.id == id } ?: ALL_SAMPLES[0]
    }
}
