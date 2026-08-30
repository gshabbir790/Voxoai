package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.TtsGenerationResult
import com.example.ui.theme.*
import java.io.File

@Composable
fun AudioPlayerBar(
    isPlaying: Boolean,
    currentPositionMs: Int,
    durationMs: Int,
    playbackSpeed: Float,
    isPlayingMixedAudio: Boolean,
    waveformPoints: List<Float>,
    isGenerating: Boolean,
    generationProgress: TtsGenerationResult.Progress?,
    voiceOnlyFile: File?,
    mixedAudioFile: File?,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onPlaybackSpeedChanged: (Float) -> Unit,
    onToggleAudioTrackAB: (Boolean) -> Unit,
    onGenerateVoice: () -> Unit,
    onOpenExport: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasAudio = voiceOnlyFile != null || mixedAudioFile != null
    val progressRatio = if (durationMs > 0) (currentPositionMs.toFloat() / durationMs).coerceIn(0f, 1f) else 0f

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("audio_player_bar"),
        color = ObsidianSurface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorderSubtle)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Engine Output Status Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (hasAudio) EmeraldSuccess else AmberToken)
                    )
                    Text(
                        text = if (hasAudio) "ENGINE OUTPUT: 48KHZ HD AUDIO" else "ENGINE READY: 48KHZ GEMINI FLASH",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            color = TextMutedDark
                        )
                    )
                }

                if (hasAudio) {
                    Text(
                        text = if (isPlayingMixedAudio) "MASTER MIX ACTIVE" else "DRY VOICE ACTIVE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = IceBlueAccent
                        )
                    )
                }
            }

            // Generation Progress Banner (if active)
            if (isGenerating) {
                Surface(
                    color = Color(0x1F82B1FF),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IceBlueAccent.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = generationProgress?.percent ?: 0.5f,
                            modifier = Modifier.size(20.dp),
                            color = IceBlueAccent,
                            strokeWidth = 2.5.dp
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = generationProgress?.stepTitle ?: "Synthesizing Studio Audio...",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = IceBlueAccent)
                            )
                            Text(
                                text = "Step ${generationProgress?.currentStep ?: 1} of ${generationProgress?.totalSteps ?: 5} • AI Acoustic Synthesis",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp, color = TextMutedDark)
                            )
                        }
                    }
                }
            }

            val unplayedBarColor = Color(0x26FFFFFF)
            val playedVoiceColor = IceBlueAccent
            val playedMixedColor = LavenderAccent

            // Visualizer Waveform Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0D0D0D))
                    .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(8.dp))
                    .clickable(enabled = hasAudio) {
                        // Click seeking on waveform
                    }
                    .testTag("waveform_visualizer"),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 4.dp)) {
                    val count = waveformPoints.size
                    if (count == 0) return@Canvas
                    val barWidth = size.width / count
                    val spacing = 2.dp.toPx()
                    val actualBarWidth = (barWidth - spacing).coerceAtLeast(1.5f)
                    val centerY = size.height / 2f

                    for (i in 0 until count) {
                        val barRatio = waveformPoints[i]
                        val barHeight = (size.height * 0.85f * barRatio).coerceAtLeast(3f)
                        val x = i * barWidth + spacing / 2f
                        val isPlayed = (i.toFloat() / count) <= progressRatio

                        val barColor = if (isPlayed) {
                            if (isPlayingMixedAudio) playedMixedColor else playedVoiceColor
                        } else {
                            unplayedBarColor
                        }

                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(x, centerY - barHeight / 2f),
                            size = Size(actualBarWidth, barHeight),
                            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                        )
                    }
                }
            }

            // Playback Slider & Time Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = formatTime(currentPositionMs),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        color = TextPrimaryDark
                    )
                )

                Slider(
                    value = if (durationMs > 0) currentPositionMs.toFloat() else 0f,
                    onValueChange = { onSeekTo(it.toInt()) },
                    valueRange = 0f..durationMs.toFloat().coerceAtLeast(1f),
                    enabled = hasAudio,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = if (isPlayingMixedAudio) LavenderAccent else IceBlueAccent,
                        activeTrackColor = if (isPlayingMixedAudio) LavenderAccent else IceBlueAccent,
                        inactiveTrackColor = Color(0x26FFFFFF)
                    )
                )

                Text(
                    text = formatTime(durationMs),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        color = TextMutedDark
                    )
                )
            }

            // Primary Control Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // A/B Audio Switcher (Mixed vs Voice Only)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF1A1A1A))
                        .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(6.dp))
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onToggleAudioTrackAB(false) },
                        color = if (!isPlayingMixedAudio) IceBlueAccent else Color.Transparent
                    ) {
                        Text(
                            text = "VOICE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = if (!isPlayingMixedAudio) Color.Black else TextMutedDark
                            ),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onToggleAudioTrackAB(true) },
                        color = if (isPlayingMixedAudio) LavenderAccent else Color.Transparent
                    ) {
                        Text(
                            text = "MIX",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = if (isPlayingMixedAudio) Color.Black else TextMutedDark
                            ),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }

                // Playback Speed Selector Chips
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    listOf(0.75f, 1.0f, 1.25f, 1.5f).forEach { spd ->
                        val isSel = spd == playbackSpeed
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .border(1.dp, if (isSel) IceBlueAccent else ObsidianBorderSubtle, RoundedCornerShape(4.dp))
                                .clickable { onPlaybackSpeedChanged(spd) },
                            color = if (isSel) Color(0x1F82B1FF) else Color(0xFF141414)
                        ) {
                            Text(
                                text = "${spd}x",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) IceBlueAccent else TextMutedDark
                                ),
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                // Action Buttons: Play/Pause, Generate, Export
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Play / Pause FAB
                    IconButton(
                        onClick = onTogglePlayPause,
                        enabled = hasAudio && !isGenerating,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (hasAudio) Brush.linearGradient(listOf(IceBlueAccent, LavenderAccent))
                                else Brush.linearGradient(listOf(Color(0xFF262626), Color(0xFF1A1A1A)))
                            )
                            .testTag("play_pause_button")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = if (hasAudio) Color.Black else TextMutedDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Main Generate Speech Button
                    Button(
                        onClick = onGenerateVoice,
                        enabled = !isGenerating,
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("generate_audio_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = IceBlueAccent,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isGenerating) "GENERATING..." else "GENERATE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                fontSize = 11.sp
                            )
                        )
                    }

                    // Export / Download Button
                    IconButton(
                        onClick = onOpenExport,
                        enabled = hasAudio && !isGenerating,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF141414))
                            .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(8.dp))
                            .testTag("export_audio_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Download,
                            contentDescription = "Export Audio",
                            tint = if (hasAudio) EmeraldSuccess else TextMutedDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun formatTime(ms: Int): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
