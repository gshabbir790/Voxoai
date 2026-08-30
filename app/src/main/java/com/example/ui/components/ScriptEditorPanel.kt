package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.model.SceneItem
import com.example.ui.theme.*
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.RoseDanger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScriptEditorPanel(
    scriptText: String,
    language: Language,
    scenes: List<SceneItem>,
    activeSceneIndex: Int,
    customVoiceDirection: String,
    isMultiSceneMode: Boolean,
    onScriptChanged: (String) -> Unit,
    onCustomVoiceDirectionChanged: (String) -> Unit,
    onSelectScene: (Int) -> Unit,
    onAddScene: () -> Unit,
    onDuplicateScene: (Int) -> Unit,
    onDeleteScene: (Int) -> Unit,
    onInsertTag: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDirectorNotes by remember { mutableStateOf(false) }

    // Metrics calculations
    val charCount = scriptText.length
    val wordCount = if (scriptText.isBlank()) 0 else scriptText.trim().split(Regex("\\s+")).size
    val sentenceCount = if (scriptText.isBlank()) 0 else scriptText.split(Regex("[.!?۔\n]+")).filter { it.isNotBlank() }.size
    val estimatedDurationSec = (charCount / 14.5f).toInt().coerceAtLeast(0)

    val layoutDirection = if (language.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    val textAlignment = if (language.isRtl) TextAlign.Right else TextAlign.Left

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("script_editor_panel"),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorderSubtle)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header: Scene Label + AI Director trigger
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0x26FFFFFF)
                    ) {
                        Text(
                            text = "SCENE 0${activeSceneIndex + 1}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = TextPrimaryDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = scenes.getOrNull(activeSceneIndex)?.title ?: "Script Editor",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondaryDark
                        )
                    )
                }

                // Scene Count Badge
                Text(
                    text = "${scenes.size} SCENE${if (scenes.size > 1) "S" else ""}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        color = IceBlueAccent
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0x1482B1FF))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            // Scene Tabs Carousel
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                LazyRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    itemsIndexed(scenes) { index, scene ->
                        val isActive = index == activeSceneIndex
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .border(
                                    1.dp,
                                    if (isActive) IceBlueAccent else ObsidianBorderSubtle,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { onSelectScene(index) }
                                .testTag("scene_tab_$index"),
                            color = if (isActive) Color(0x1F82B1FF) else Color(0xFF1A1A1A)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = scene.title.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isActive) IceBlueAccent else TextSecondaryDark
                                    )
                                )

                                if (scenes.size > 1 && isActive) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Delete Scene",
                                        tint = RoseDanger,
                                        modifier = Modifier
                                            .size(13.dp)
                                            .clickable { onDeleteScene(index) }
                                    )
                                }
                            }
                        }
                    }
                }

                // Add Scene Button
                IconButton(
                    onClick = onAddScene,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF1A1A1A))
                        .testTag("add_scene_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Scene",
                        tint = IceBlueAccent,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Duplicate Scene Button
                IconButton(
                    onClick = { onDuplicateScene(activeSceneIndex) },
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF1A1A1A))
                        .testTag("duplicate_scene_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = "Duplicate Scene",
                        tint = TextMutedDark,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Quick Insertion Toolbar (Pauses & Emphasis)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1A1A1A))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "STUDIO PROSODY TAGS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = TextMutedDark
                        )
                    )

                    TextButton(
                        onClick = { showDirectorNotes = !showDirectorNotes },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(20.dp)
                    ) {
                        Text(
                            text = if (showDirectorNotes) "Hide Director Notes" else "+ Director Notes",
                            style = MaterialTheme.typography.labelSmall.copy(color = IceBlueAccent, fontSize = 10.sp)
                        )
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        ProsodyTagChip(label = "[pause 0.5s]", onClick = { onInsertTag("[pause 0.5s]") })
                    }
                    item {
                        ProsodyTagChip(label = "[pause 1.0s]", onClick = { onInsertTag("[pause 1.0s]") })
                    }
                    item {
                        ProsodyTagChip(label = "[pause 2.0s]", onClick = { onInsertTag("[pause 2.0s]") })
                    }
                    item {
                        ProsodyTagChip(label = "[emphasis: strong]", onClick = { onInsertTag("[emphasis: strong]") })
                    }
                    item {
                        ProsodyTagChip(label = "[emphasis: soft]", onClick = { onInsertTag("[emphasis: soft]") })
                    }
                    item {
                        ProsodyTagChip(label = "[whisper]", onClick = { onInsertTag("[whisper]") })
                    }
                }
            }

            // Expandable Custom Director Notes Input
            AnimatedVisibility(visible = showDirectorNotes) {
                OutlinedTextField(
                    value = customVoiceDirection,
                    onValueChange = onCustomVoiceDirectionChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("director_notes_input"),
                    placeholder = {
                        Text(
                            "e.g. Speak with intense cinematic mystery, pausing dramatically before the climax...",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                        )
                    },
                    label = { Text("AI Director Instructions", style = MaterialTheme.typography.labelSmall) },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IceBlueAccent,
                        unfocusedBorderColor = ObsidianBorderSubtle,
                        focusedContainerColor = Color(0xFF141414),
                        unfocusedContainerColor = Color(0xFF141414)
                    ),
                    maxLines = 3
                )
            }

            // Main Script Input Box with RTL/LTR Support
            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                OutlinedTextField(
                    value = scriptText,
                    onValueChange = onScriptChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp, max = 280.dp)
                        .testTag("main_script_input"),
                    placeholder = {
                        Text(
                            text = language.placeholderText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMutedDark,
                            textAlign = textAlignment
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = TextPrimaryDark,
                        textAlign = textAlignment,
                        lineHeight = 24.sp
                    ),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IceBlueAccent,
                        unfocusedBorderColor = ObsidianBorderSubtle,
                        focusedContainerColor = Color(0xFF0D0D0D),
                        unfocusedContainerColor = Color(0xFF0D0D0D)
                    )
                )
            }

            // Footer Status & Live Counters (High Density Mono Statistics)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Script Statistics Overlay
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "WORDS: $wordCount",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            color = TextMutedDark
                        )
                    )
                    Text("•", color = TextMutedDark, fontSize = 9.sp)
                    Text(
                        text = "CHARS: $charCount/10000",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            color = if (charCount > 7500) RoseDanger else TextMutedDark
                        )
                    )
                    Text("•", color = TextMutedDark, fontSize = 9.sp)
                    Text(
                        text = "EST: 0:${if (estimatedDurationSec < 10) "0$estimatedDurationSec" else "$estimatedDurationSec"}s",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            color = IceBlueAccent
                        )
                    )
                }

                // Clear / Reset Action
                if (scriptText.isNotBlank()) {
                    Text(
                        text = "CLEAR",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextMutedDark,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier
                            .clickable { onScriptChanged("") }
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProsodyTagChip(
    label: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(4.dp))
            .clickable { onClick() },
        color = Color(0xFF141414)
    ) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp,
                color = IceBlueAccent
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}
