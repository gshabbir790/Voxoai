package com.example.ui.components

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
import com.example.data.model.Language
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioTopBar(
    projectName: String,
    selectedLanguage: Language,
    isDarkTheme: Boolean,
    isDemoMode: Boolean,
    onLanguageSelected: (Language) -> Unit,
    onThemeToggle: () -> Unit,
    onOpenAiDirector: () -> Unit,
    onOpenSampleProjects: () -> Unit,
    onSaveProject: () -> Unit,
    onOpenExport: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLanguageDropdown by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("studio_top_bar"),
        color = ObsidianBackground,
        tonalElevation = 0.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorderSubtle)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand Logo & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(IceBlueAccent, LavenderAccent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MicExternalOn,
                            contentDescription = "Voxora Studio Logo",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "VOXORA STUDIO",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "AI VOICE ENGINE",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp
                            ),
                            color = TextMutedDark
                        )
                    }
                }

                // Action Bar (Language Selector, AI Director, Presets, Actions)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Token / Quota Badge
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = Color(0xFF1A1A1A),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x1AFFFFFF))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Token,
                                contentDescription = "Tokens",
                                tint = AmberToken,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "12.4K",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = TextPrimaryDark
                            )
                        }
                    }

                    // AI Voice Director Action Button
                    Button(
                        onClick = onOpenAiDirector,
                        modifier = Modifier
                            .height(32.dp)
                            .testTag("ai_director_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0x1482B1FF),
                            contentColor = IceBlueAccent
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("AI Director", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold))
                    }

                    // Strict 7 Languages Selector Dropdown
                    Box {
                        Surface(
                            modifier = Modifier
                                .height(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(6.dp))
                                .clickable { showLanguageDropdown = true }
                                .testTag("language_selector_button"),
                            color = Color(0xFF141414)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "Language",
                                    tint = IceBlueAccent,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${selectedLanguage.displayName}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showLanguageDropdown,
                            onDismissRequest = { showLanguageDropdown = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            Text(
                                text = "SUPPORTED STUDIO LANGUAGES",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                            Divider(color = MaterialTheme.colorScheme.outline)

                            Language.ALL_SUPPORTED_LANGUAGES.forEach { lang ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${lang.displayName} — ${lang.nativeName}",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (lang == selectedLanguage) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (lang == selectedLanguage) CyanAccent else MaterialTheme.colorScheme.onSurface
                                                )
                                            )
                                            if (lang.isRtl) {
                                                Text(
                                                    text = "RTL",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(3.dp))
                                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        onLanguageSelected(lang)
                                        showLanguageDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    // Sample Projects button
                    IconButton(
                        onClick = onOpenSampleProjects,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("sample_projects_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FolderSpecial,
                            contentDescription = "Sample Projects",
                            tint = AmberWarning,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Save Project
                    IconButton(
                        onClick = onSaveProject,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("save_project_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Save,
                            contentDescription = "Save Project",
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Theme Toggle
                    IconButton(
                        onClick = onThemeToggle,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("theme_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Mode Badge Banner (if in Demo Mode)
            if (isDemoMode) {
                Surface(
                    color = AmberWarning.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = AmberWarning,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "STUDIO DEMO MODE: Synthesized local preview active. Connect your Gemini API Key in Secrets panel for Ultra-HD Gemini 2.5 Flash TTS.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = AmberWarning
                            )
                        }
                    }
                }
            }
        }
    }
}
