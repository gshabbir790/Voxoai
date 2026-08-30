package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MusicTrack
import com.example.ui.theme.*

@Composable
fun AudioMixerPanel(
    selectedMusicTrack: MusicTrack,
    voiceVolume: Float,
    musicVolume: Float,
    sfxVolume: Float,
    autoDuckingEnabled: Boolean,
    duckingAmountPercent: Int,
    duckingAttackMs: Int,
    duckingReleaseMs: Int,
    onVolumeChanged: (Float, Float, Float) -> Unit,
    onAutoDuckingChanged: (Boolean, Int, Int, Int) -> Unit,
    onOpenMusicLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("audio_mixer_panel"),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorderSubtle)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = IceBlueAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "3-TRACK STUDIO AUDIO MIXER",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = TextPrimaryDark
                        )
                    )
                }

                // Music Library Selector Button
                Button(
                    onClick = onOpenMusicLibrary,
                    modifier = Modifier.height(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x1F82B1FF),
                        contentColor = IceBlueAccent
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IceBlueAccent.copy(alpha = 0.3f))
                ) {
                    Icon(imageVector = Icons.Default.LibraryMusic, contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "MUSIC LIBRARY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            // Track 1: Voice Track
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Mic, contentDescription = "Voice", tint = IceBlueAccent, modifier = Modifier.size(16.dp))
                Text(
                    text = "VOICE: ${(voiceVolume * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        color = IceBlueAccent
                    ),
                    modifier = Modifier.width(80.dp)
                )
                Slider(
                    value = voiceVolume,
                    onValueChange = { onVolumeChanged(it, musicVolume, sfxVolume) },
                    valueRange = 0f..1.5f,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = IceBlueAccent,
                        activeTrackColor = IceBlueAccent,
                        inactiveTrackColor = Color(0x26FFFFFF)
                    )
                )
            }

            // Track 2: Background Music Track
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Default.MusicNote, contentDescription = "Music", tint = LavenderAccent, modifier = Modifier.size(16.dp))
                Column(modifier = Modifier.width(80.dp)) {
                    Text(
                        "MUSIC: ${(musicVolume * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            color = LavenderAccent
                        )
                    )
                    Text(
                        text = selectedMusicTrack.title.uppercase(),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 8.sp, color = TextMutedDark),
                        maxLines = 1
                    )
                }
                Slider(
                    value = musicVolume,
                    onValueChange = { onVolumeChanged(voiceVolume, it, sfxVolume) },
                    valueRange = 0f..1.0f,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = LavenderAccent,
                        activeTrackColor = LavenderAccent,
                        inactiveTrackColor = Color(0x26FFFFFF)
                    )
                )
            }

            // Track 3: SFX Track
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Default.GraphicEq, contentDescription = "SFX", tint = AmberToken, modifier = Modifier.size(16.dp))
                Text(
                    text = "SFX: ${(sfxVolume * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        color = AmberToken
                    ),
                    modifier = Modifier.width(80.dp)
                )
                Slider(
                    value = sfxVolume,
                    onValueChange = { onVolumeChanged(voiceVolume, musicVolume, it) },
                    valueRange = 0f..1.0f,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = AmberToken,
                        activeTrackColor = AmberToken,
                        inactiveTrackColor = Color(0x26FFFFFF)
                    )
                )
            }

            HorizontalDivider(color = ObsidianBorderSubtle)

            // Auto-Ducking Subsystem
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(imageVector = Icons.Default.VolumeDown, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(14.dp))
                        Text(
                            text = "AUTO-DUCKING SYSTEM",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = TextPrimaryDark
                            )
                        )
                    }
                    Text(
                        text = "Lowers music floor dynamically when vocal acoustic energy is detected",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp, color = TextMutedDark)
                    )
                }

                Switch(
                    checked = autoDuckingEnabled,
                    onCheckedChange = { onAutoDuckingChanged(it, duckingAmountPercent, duckingAttackMs, duckingReleaseMs) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = EmeraldSuccess,
                        checkedTrackColor = EmeraldSuccess.copy(alpha = 0.3f),
                        uncheckedThumbColor = TextMutedDark,
                        uncheckedTrackColor = Color(0xFF1E1E1E)
                    )
                )
            }

            if (autoDuckingEnabled) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "DUCKING: $duckingAmountPercent%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            color = EmeraldSuccess
                        ),
                        modifier = Modifier.width(80.dp)
                    )
                    Slider(
                        value = duckingAmountPercent.toFloat(),
                        onValueChange = { onAutoDuckingChanged(autoDuckingEnabled, it.toInt(), duckingAttackMs, duckingReleaseMs) },
                        valueRange = 20f..95f,
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = EmeraldSuccess,
                            activeTrackColor = EmeraldSuccess,
                            inactiveTrackColor = Color(0x26FFFFFF)
                        )
                    )
                }
            }
        }
    }
}
