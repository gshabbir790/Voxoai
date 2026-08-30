package com.example.data.model

enum class Language(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val isRtl: Boolean,
    val supportedAccents: List<String>,
    val placeholderText: String
) {
    URDU(
        code = "ur",
        displayName = "Urdu",
        nativeName = "اردو",
        isRtl = true,
        supportedAccents = listOf("Pakistani Urdu", "Neutral Urdu"),
        placeholderText = "یہاں اپنا اسکرپٹ لکھیں یا چسپاں کریں... ووکسورا اسٹوڈیو آپ کے الفاظ کو جاندار آواز میں تبدیل کرے گا۔"
    ),
    ENGLISH(
        code = "en",
        displayName = "English",
        nativeName = "English",
        isRtl = false,
        supportedAccents = listOf("American English", "British English", "Indian English", "Neutral English"),
        placeholderText = "Enter or paste your script here... Voxora AI Studio will transform every word into a professional voice-over."
    ),
    ARABIC(
        code = "ar",
        displayName = "Arabic",
        nativeName = "العربية",
        isRtl = true,
        supportedAccents = listOf("Modern Standard Arabic", "Neutral Arabic"),
        placeholderText = "أدخل النص الخاص بك هنا... استوديو فوكسورا للذكاء الاصطناعي يحول كل كلمة إلى صوت احترافي."
    ),
    PERSIAN(
        code = "fa",
        displayName = "Persian/Farsi",
        nativeName = "فارسی",
        isRtl = true,
        supportedAccents = listOf("Standard Persian"),
        placeholderText = "متن فیلم‌نامه خود را اینجا وارد کنید... استودیو وکسورا کلمات شما را به صدای حرفه‌ای تبدیل می‌کند."
    ),
    PASHTO(
        code = "ps",
        displayName = "Pashto",
        nativeName = "پښتو",
        isRtl = true,
        supportedAccents = listOf("Standard Pashto"),
        placeholderText = "خپله لیکنه دلته ولیکئ... ووکسورا سټوډیو ستاسو خبرې په اغیزمن او مسلکي غږ بدلوي."
    ),
    CHINESE(
        code = "zh",
        displayName = "Chinese/Mandarin",
        nativeName = "中文",
        isRtl = false,
        supportedAccents = listOf("Mandarin Chinese"),
        placeholderText = "在此输入或粘贴您的脚本文本... Voxora AI 工作室将为每一个字赋予饱满生动的专业人声。"
    ),
    HINDI(
        code = "hi",
        displayName = "Hindi",
        nativeName = "हिन्दी",
        isRtl = false,
        supportedAccents = listOf("Standard Hindi"),
        placeholderText = "यहाँ अपनी स्क्रिप्ट दर्ज करें या पेस्ट करें... वोक्सोरा एआई स्टूडियो हर शब्द को जीवंत आवाज़ में बदल देगा।"
    );

    companion object {
        val ALL_SUPPORTED_LANGUAGES = listOf(URDU, ENGLISH, ARABIC, PERSIAN, PASHTO, CHINESE, HINDI)
        fun fromCode(code: String): Language = values().firstOrNull { it.code == code } ?: ENGLISH
    }
}
