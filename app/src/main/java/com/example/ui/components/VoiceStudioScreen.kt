package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OneClickPreset
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudioUiState
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun VoiceStudioScreen(
    uiState: StudioUiState,
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    var studioSectionTab by remember { mutableStateOf(0) } // 0: Script & Scenes, 1: Voice & Styles, 2: Audio Mixer

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWideScreen = maxWidth > 800.dp

        if (isWideScreen) {
            // Wide Screen Desktop / Tablet Layout (Side by Side Multi-Column)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Left Column: Script Editor & One-Click Presets
                Column(
                    modifier = Modifier
                        .weight(1.1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OneClickPresetsBar(
                        onSelectPreset = { viewModel.applyOneClickPreset(it) }
                    )

                    ScriptEditorPanel(
                        scriptText = uiState.scriptText,
                        language = uiState.selectedLanguage,
                        scenes = uiState.scenes,
                        activeSceneIndex = uiState.activeSceneIndex,
                        customVoiceDirection = uiState.customVoiceDirection,
                        isMultiSceneMode = uiState.isMultiSceneMode,
                        onScriptChanged = { viewModel.setScriptText(it) },
                        onCustomVoiceDirectionChanged = { viewModel.setCustomVoiceDirection(it) },
                        onSelectScene = { viewModel.selectScene(it) },
                        onAddScene = { viewModel.addScene() },
                        onDuplicateScene = { viewModel.duplicateScene(it) },
                        onDeleteScene = { viewModel.deleteScene(it) },
                        onInsertTag = { viewModel.insertScriptTag(it) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Right Column: Voice Controls & Mixer Scrollable
                LazyColumn(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        VoiceControlsPanel(
                            selectedModel = uiState.selectedModel,
                            selectedVoice = uiState.selectedVoice,
                            selectedAccent = uiState.selectedAccent,
                            selectedLanguage = uiState.selectedLanguage,
                            selectedStyle = uiState.selectedStyle,
                            emotionBlend = uiState.emotionBlend,
                            speakingControls = uiState.speakingControls,
                            voiceFilterCategory = uiState.voiceFilterCategory,
                            styleFilterCategory = uiState.styleFilterCategory,
                            onModelSelected = { viewModel.setTtsModel(it) },
                            onVoiceSelected = { viewModel.setVoice(it) },
                            onAccentSelected = { viewModel.setAccent(it) },
                            onStyleSelected = { viewModel.setSpeakingStyle(it) },
                            onPrimaryEmotionChanged = { em, pct -> viewModel.setPrimaryEmotion(em, pct) },
                            onSecondaryEmotionChanged = { em, pct -> viewModel.setSecondaryEmotion(em, pct) },
                            onEmotionIntensityChanged = { viewModel.setEmotionIntensity(it) },
                            onSpeakingControlsChanged = { spd, pit, en, pau -> viewModel.setSpeakingControls(spd, pit, en, pau) },
                            onVoiceFilterCategoryChanged = { viewModel.setVoiceFilterCategory(it) },
                            onStyleFilterCategoryChanged = { viewModel.setStyleFilterCategory(it) }
                        )
                    }

                    item {
                        AudioMixerPanel(
                            selectedMusicTrack = uiState.selectedMusicTrack,
                            voiceVolume = uiState.voiceVolume,
                            musicVolume = uiState.musicVolume,
                            sfxVolume = uiState.sfxVolume,
                            autoDuckingEnabled = uiState.autoDuckingEnabled,
                            duckingAmountPercent = uiState.duckingAmountPercent,
                            duckingAttackMs = uiState.duckingAttackMs,
                            duckingReleaseMs = uiState.duckingReleaseMs,
                            onVolumeChanged = { v, m, s -> viewModel.setAudioVolumes(v, m, s) },
                            onAutoDuckingChanged = { en, amt, att, rel -> viewModel.setAutoDucking(en, amt, att, rel) },
                            onOpenMusicLibrary = { viewModel.setShowMusicLibraryDialog(true) }
                        )
                    }
                }
            }
        } else {
            // Mobile Compact Vertical Layout with Segmented Studio Tabs
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // One-Click Presets Bar
                OneClickPresetsBar(
                    onSelectPreset = { viewModel.applyOneClickPreset(it) }
                )

                // Segmented Studio Navigation Switcher (High Density Workspace Tabs)
                TabRow(
                    selectedTabIndex = studioSectionTab,
                    containerColor = ObsidianSurface,
                    contentColor = IceBlueAccent,
                    indicator = { tabPositions ->
                        if (studioSectionTab < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[studioSectionTab]),
                                color = IceBlueAccent,
                                height = 2.5.dp
                            )
                        }
                    },
                    divider = { Divider(color = ObsidianBorderSubtle) }
                ) {
                    Tab(
                        selected = studioSectionTab == 0,
                        onClick = { studioSectionTab = 0 },
                        text = {
                            Text(
                                "SCRIPT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp,
                                    fontSize = 11.sp
                                )
                            )
                        },
                        icon = { Icon(imageVector = Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        selectedContentColor = IceBlueAccent,
                        unselectedContentColor = TextMutedDark
                    )
                    Tab(
                        selected = studioSectionTab == 1,
                        onClick = { studioSectionTab = 1 },
                        text = {
                            Text(
                                "VOICE & STYLE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp,
                                    fontSize = 11.sp
                                )
                            )
                        },
                        icon = { Icon(imageVector = Icons.Default.RecordVoiceOver, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        selectedContentColor = IceBlueAccent,
                        unselectedContentColor = TextMutedDark
                    )
                    Tab(
                        selected = studioSectionTab == 2,
                        onClick = { studioSectionTab = 2 },
                        text = {
                            Text(
                                "AUDIO MIX",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp,
                                    fontSize = 11.sp
                                )
                            )
                        },
                        icon = { Icon(imageVector = Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        selectedContentColor = IceBlueAccent,
                        unselectedContentColor = TextMutedDark
                    )
                }

                // Section Content
                Box(modifier = Modifier.weight(1f)) {
                    when (studioSectionTab) {
                        0 -> {
                            ScriptEditorPanel(
                                scriptText = uiState.scriptText,
                                language = uiState.selectedLanguage,
                                scenes = uiState.scenes,
                                activeSceneIndex = uiState.activeSceneIndex,
                                customVoiceDirection = uiState.customVoiceDirection,
                                isMultiSceneMode = uiState.isMultiSceneMode,
                                onScriptChanged = { viewModel.setScriptText(it) },
                                onCustomVoiceDirectionChanged = { viewModel.setCustomVoiceDirection(it) },
                                onSelectScene = { viewModel.selectScene(it) },
                                onAddScene = { viewModel.addScene() },
                                onDuplicateScene = { viewModel.duplicateScene(it) },
                                onDeleteScene = { viewModel.deleteScene(it) },
                                onInsertTag = { viewModel.insertScriptTag(it) },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        1 -> {
                            LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                item {
                                    VoiceControlsPanel(
                                        selectedModel = uiState.selectedModel,
                                        selectedVoice = uiState.selectedVoice,
                                        selectedAccent = uiState.selectedAccent,
                                        selectedLanguage = uiState.selectedLanguage,
                                        selectedStyle = uiState.selectedStyle,
                                        emotionBlend = uiState.emotionBlend,
                                        speakingControls = uiState.speakingControls,
                                        voiceFilterCategory = uiState.voiceFilterCategory,
                                        styleFilterCategory = uiState.styleFilterCategory,
                                        onModelSelected = { viewModel.setTtsModel(it) },
                                        onVoiceSelected = { viewModel.setVoice(it) },
                                        onAccentSelected = { viewModel.setAccent(it) },
                                        onStyleSelected = { viewModel.setSpeakingStyle(it) },
                                        onPrimaryEmotionChanged = { em, pct -> viewModel.setPrimaryEmotion(em, pct) },
                                        onSecondaryEmotionChanged = { em, pct -> viewModel.setSecondaryEmotion(em, pct) },
                                        onEmotionIntensityChanged = { viewModel.setEmotionIntensity(it) },
                                        onSpeakingControlsChanged = { spd, pit, en, pau -> viewModel.setSpeakingControls(spd, pit, en, pau) },
                                        onVoiceFilterCategoryChanged = { viewModel.setVoiceFilterCategory(it) },
                                        onStyleFilterCategoryChanged = { viewModel.setStyleFilterCategory(it) }
                                    )
                                }
                            }
                        }
                        2 -> {
                            LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                item {
                                    AudioMixerPanel(
                                        selectedMusicTrack = uiState.selectedMusicTrack,
                                        voiceVolume = uiState.voiceVolume,
                                        musicVolume = uiState.musicVolume,
                                        sfxVolume = uiState.sfxVolume,
                                        autoDuckingEnabled = uiState.autoDuckingEnabled,
                                        duckingAmountPercent = uiState.duckingAmountPercent,
                                        duckingAttackMs = uiState.duckingAttackMs,
                                        duckingReleaseMs = uiState.duckingReleaseMs,
                                        onVolumeChanged = { v, m, s -> viewModel.setAudioVolumes(v, m, s) },
                                        onAutoDuckingChanged = { en, amt, att, rel -> viewModel.setAutoDucking(en, amt, att, rel) },
                                        onOpenMusicLibrary = { viewModel.setShowMusicLibraryDialog(true) }
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

@Composable
fun OneClickPresetsBar(
    onSelectPreset: (OneClickPreset) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(OneClickPreset.ALL_PRESETS) { preset ->
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(6.dp))
                    .clickable { onSelectPreset(preset) }
                    .testTag("preset_chip_${preset.id}"),
                color = Color(0xFF141414)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(preset.iconEmoji, fontSize = 11.sp)
                    Text(
                        text = preset.title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = TextPrimaryDark
                        )
                    )
                }
            }
        }
    }
}
