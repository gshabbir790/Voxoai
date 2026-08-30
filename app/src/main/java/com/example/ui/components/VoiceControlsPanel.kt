package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceControlsPanel(
    selectedModel: TtsModel,
    selectedVoice: GeminiVoice,
    selectedAccent: String,
    selectedLanguage: Language,
    selectedStyle: SpeakingStyle,
    emotionBlend: EmotionBlend,
    speakingControls: SpeakingControls,
    voiceFilterCategory: VoiceCategory?,
    styleFilterCategory: StyleCategory?,
    onModelSelected: (TtsModel) -> Unit,
    onVoiceSelected: (GeminiVoice) -> Unit,
    onAccentSelected: (String) -> Unit,
    onStyleSelected: (SpeakingStyle) -> Unit,
    onPrimaryEmotionChanged: (Emotion, Int) -> Unit,
    onSecondaryEmotionChanged: (Emotion, Int) -> Unit,
    onEmotionIntensityChanged: (Int) -> Unit,
    onSpeakingControlsChanged: (Float, PitchSetting, EnergySetting, PausingSetting) -> Unit,
    onVoiceFilterCategoryChanged: (VoiceCategory?) -> Unit,
    onStyleFilterCategoryChanged: (StyleCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedSection by remember { mutableStateOf<String?>("voice") } // "model", "voice", "style", "emotion", "pacing"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("voice_controls_panel"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. TTS Model Selection Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(imageVector = Icons.Default.Memory, contentDescription = null, tint = IceBlueAccent, modifier = Modifier.size(16.dp))
                        Text(
                            "TTS ENGINE & MODEL",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = TextPrimaryDark
                            )
                        )
                    }
                    Text(
                        text = selectedModel.quality.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            color = IceBlueAccent
                        )
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    TtsModel.AVAILABLE_MODELS.forEach { model ->
                        val isSelected = model.id == selectedModel.id
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) IceBlueAccent else ObsidianBorderSubtle,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { onModelSelected(model) }
                                .testTag("model_card_${model.id}"),
                            color = if (isSelected) Color(0x1F82B1FF) else Color(0xFF141414)
                        ) {
                            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = model.name,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) IceBlueAccent else TextPrimaryDark
                                    )
                                )
                                Text(
                                    text = model.recommendedUse,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp, color = TextMutedDark),
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Official Gemini Voices Gallery (30 voices)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(imageVector = Icons.Default.RecordVoiceOver, contentDescription = null, tint = IceBlueAccent, modifier = Modifier.size(16.dp))
                        Text(
                            "VOICE GALLERY (${GeminiVoice.ALL_VOICES.size})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = TextPrimaryDark
                            )
                        )
                    }
                    Text(
                        text = "${selectedVoice.name} • ${selectedVoice.gender}".uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = IceBlueAccent,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }

                // Voice Category Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        FilterChip(
                            selected = voiceFilterCategory == null,
                            onClick = { onVoiceFilterCategoryChanged(null) },
                            label = { Text("ALL (${GeminiVoice.ALL_VOICES.size})", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold)) }
                        )
                    }
                    items(VoiceCategory.values()) { cat ->
                        FilterChip(
                            selected = voiceFilterCategory == cat,
                            onClick = { onVoiceFilterCategoryChanged(if (voiceFilterCategory == cat) null else cat) },
                            label = { Text(cat.label.uppercase(), style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold)) }
                        )
                    }
                }

                // Voice Cards Carousel / Grid
                val filteredVoices = GeminiVoice.ALL_VOICES.filter {
                    voiceFilterCategory == null || it.category == voiceFilterCategory
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filteredVoices) { voice ->
                        val isSelected = voice.name == selectedVoice.name
                        Surface(
                            modifier = Modifier
                                .width(160.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) IceBlueAccent else ObsidianBorderSubtle,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { onVoiceSelected(voice) }
                                .testTag("voice_card_${voice.name}"),
                            color = if (isSelected) Color(0x1F82B1FF) else Color(0xFF141414)
                        ) {
                            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = voice.name,
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) IceBlueAccent else TextPrimaryDark
                                        )
                                    )
                                    Text(
                                        text = voice.gender.uppercase(),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextMutedDark
                                        ),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(Color(0xFF262626))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                                Text(
                                    text = voice.characterTitle,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) IceBlueAccent else TextSecondaryDark
                                    ),
                                    maxLines = 1
                                )
                                Text(
                                    text = voice.description,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp, color = TextMutedDark),
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }

                // Adaptive Accent Selection
                if (selectedLanguage.supportedAccents.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "ACCENT / DIALECT NUANCE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = TextMutedDark
                            )
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            items(selectedLanguage.supportedAccents) { accent ->
                                val isAccSelected = accent == selectedAccent
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .border(1.dp, if (isAccSelected) IceBlueAccent else ObsidianBorderSubtle, RoundedCornerShape(4.dp))
                                        .clickable { onAccentSelected(accent) },
                                    color = if (isAccSelected) Color(0x1F82B1FF) else Color(0xFF141414)
                                ) {
                                    Text(
                                        text = accent,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            fontWeight = if (isAccSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isAccSelected) IceBlueAccent else TextSecondaryDark
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Speaking Style (12 Categories, 80+ Styles)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(imageVector = Icons.Default.MicExternalOn, contentDescription = null, tint = LavenderAccent, modifier = Modifier.size(16.dp))
                        Text(
                            "SPEAKING STYLE & GENRE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = TextPrimaryDark
                            )
                        )
                    }
                    Text(
                        text = selectedStyle.name.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(color = LavenderAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    )
                }

                // Style Category Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        FilterChip(
                            selected = styleFilterCategory == null,
                            onClick = { onStyleFilterCategoryChanged(null) },
                            label = { Text("ALL", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold)) }
                        )
                    }
                    items(StyleCategory.values()) { cat ->
                        FilterChip(
                            selected = styleFilterCategory == cat,
                            onClick = { onStyleFilterCategoryChanged(if (styleFilterCategory == cat) null else cat) },
                            label = { Text(cat.label.uppercase(), style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold)) }
                        )
                    }
                }

                // Sub-Styles Chips
                val filteredStyles = SpeakingStyle.ALL_STYLES.filter {
                    styleFilterCategory == null || it.category == styleFilterCategory
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filteredStyles) { style ->
                        val isSel = style.name == selectedStyle.name
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .border(
                                    1.dp,
                                    if (isSel) LavenderAccent else ObsidianBorderSubtle,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { onStyleSelected(style) }
                                .testTag("style_chip_${style.name}"),
                            color = if (isSel) Color(0x1FB388FF) else Color(0xFF141414)
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)) {
                                Text(
                                    text = style.name,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 11.sp,
                                        color = if (isSel) LavenderAccent else TextPrimaryDark
                                    )
                                )
                                Text(
                                    text = style.description,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 8.sp, color = TextMutedDark),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Emotion & Blend Engine (20 emotions + intensity + blend %)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(imageVector = Icons.Default.Mood, contentDescription = null, tint = AmberToken, modifier = Modifier.size(16.dp))
                        Text(
                            "EMOTION & NUANCE BLEND",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = TextPrimaryDark
                            )
                        )
                    }
                    Text(
                        text = "${emotionBlend.primaryEmotion.label} (${emotionBlend.primaryPercentage}%) + ${emotionBlend.secondaryEmotion.label} (${emotionBlend.secondaryPercentage}%)".uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AmberToken,
                            fontSize = 9.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // Primary Emotion Selector
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text("PRIMARY EMOTION", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMutedDark))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(Emotion.values()) { em ->
                            val isSel = em == emotionBlend.primaryEmotion
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(1.dp, if (isSel) AmberToken else ObsidianBorderSubtle, RoundedCornerShape(4.dp))
                                    .clickable { onPrimaryEmotionChanged(em, emotionBlend.primaryPercentage) },
                                color = if (isSel) Color(0x1FFFAB00) else Color(0xFF141414)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text(em.iconEmoji, fontSize = 11.sp)
                                    Text(
                                        text = em.label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            color = if (isSel) AmberToken else TextSecondaryDark
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Secondary Emotion Blend Selector
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text("SECONDARY EMOTION (BLEND SUB-TONE)", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMutedDark))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(Emotion.values()) { em ->
                            val isSel = em == emotionBlend.secondaryEmotion
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(1.dp, if (isSel) LavenderAccent else ObsidianBorderSubtle, RoundedCornerShape(4.dp))
                                    .clickable { onSecondaryEmotionChanged(em, 30) },
                                color = if (isSel) Color(0x1FB388FF) else Color(0xFF141414)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text(em.iconEmoji, fontSize = 11.sp)
                                    Text(
                                        text = em.label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            color = if (isSel) LavenderAccent else TextSecondaryDark
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Emotion Intensity Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "INTENSITY: ${emotionBlend.intensity}%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            color = TextMutedDark
                        ),
                        modifier = Modifier.width(95.dp)
                    )
                    Slider(
                        value = emotionBlend.intensity.toFloat(),
                        onValueChange = { onEmotionIntensityChanged(it.toInt()) },
                        valueRange = 0f..100f,
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(thumbColor = AmberToken, activeTrackColor = AmberToken, inactiveTrackColor = Color(0x26FFFFFF))
                    )
                }
            }
        }

        // 5. Pacing, Speed, Pitch & Energy Controls
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                        Text(
                            "PACING & VOICE REGISTER",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = TextPrimaryDark
                            )
                        )
                    }
                    Text(
                        text = "${speakingControls.speed}X • ${speakingControls.pitch.label} PITCH",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = EmeraldSuccess,
                            fontSize = 9.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // Speed Options
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text("SPEAKING SPEED", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMutedDark))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(SpeakingControls.SPEED_OPTIONS) { spd ->
                            val isSel = spd == speakingControls.speed
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(1.dp, if (isSel) EmeraldSuccess else ObsidianBorderSubtle, RoundedCornerShape(4.dp))
                                    .clickable {
                                        onSpeakingControlsChanged(spd, speakingControls.pitch, speakingControls.energy, speakingControls.pausing)
                                    },
                                color = if (isSel) Color(0x1F00E676) else Color(0xFF141414)
                            ) {
                                Text(
                                    text = "${spd}x",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSel) EmeraldSuccess else TextSecondaryDark,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }

                // Pitch, Energy & Pausing Pickers
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Pitch
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("PITCH", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMutedDark))
                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            PitchSetting.values().forEach { p ->
                                val isSel = p == speakingControls.pitch
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .border(1.dp, if (isSel) EmeraldSuccess else ObsidianBorderSubtle, RoundedCornerShape(4.dp))
                                        .clickable {
                                            onSpeakingControlsChanged(speakingControls.speed, p, speakingControls.energy, speakingControls.pausing)
                                        },
                                    color = if (isSel) Color(0x1F00E676) else Color(0xFF141414)
                                ) {
                                    Text(
                                        text = p.label.take(1),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSel) EmeraldSuccess else TextSecondaryDark,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        ),
                                        modifier = Modifier.padding(vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Energy
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("ENERGY", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMutedDark))
                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            EnergySetting.values().forEach { en ->
                                val isSel = en == speakingControls.energy
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .border(1.dp, if (isSel) EmeraldSuccess else ObsidianBorderSubtle, RoundedCornerShape(4.dp))
                                        .clickable {
                                            onSpeakingControlsChanged(speakingControls.speed, speakingControls.pitch, en, speakingControls.pausing)
                                        },
                                    color = if (isSel) Color(0x1F00E676) else Color(0xFF141414)
                                ) {
                                    Text(
                                        text = en.label.take(1),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSel) EmeraldSuccess else TextSecondaryDark,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        ),
                                        modifier = Modifier.padding(vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Pausing
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("PAUSING", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMutedDark))
                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            PausingSetting.values().forEach { pa ->
                                val isSel = pa == speakingControls.pausing
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .border(1.dp, if (isSel) EmeraldSuccess else ObsidianBorderSubtle, RoundedCornerShape(4.dp))
                                        .clickable {
                                            onSpeakingControlsChanged(speakingControls.speed, speakingControls.pitch, speakingControls.energy, pa)
                                        },
                                    color = if (isSel) Color(0x1F00E676) else Color(0xFF141414)
                                ) {
                                    Text(
                                        text = pa.label.take(1),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSel) EmeraldSuccess else TextSecondaryDark,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        ),
                                        modifier = Modifier.padding(vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
