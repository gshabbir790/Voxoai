package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkStudioColorScheme = darkColorScheme(
    primary = IceBlueAccent,
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF1A2744),
    onPrimaryContainer = Color(0xFFD6E4FF),
    secondary = LavenderAccent,
    onSecondary = Color(0xFF1A0A3A),
    secondaryContainer = Color(0xFF2C1E4A),
    onSecondaryContainer = Color(0xFFEADBFF),
    tertiary = AmberWarning,
    onTertiary = Color(0xFF000000),
    background = ObsidianBackground,
    onBackground = TextPrimaryDark,
    surface = ObsidianSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = ObsidianSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = ObsidianBorder,
    outlineVariant = ObsidianBorderSubtle,
    error = RoseDanger,
    onError = Color.White
)

private val LightStudioColorScheme = lightColorScheme(
    primary = Color(0xFF0284C7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = Color(0xFF4F46E5),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEEF2FF),
    onSecondaryContainer = Color(0xFF3730A3),
    tertiary = Color(0xFFD97706),
    onTertiary = Color.White,
    background = LightStudioBackground,
    onBackground = TextPrimaryLight,
    surface = LightStudioSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightStudioSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = LightStudioBorder,
    outlineVariant = Color(0xFFCBD5E1),
    error = Color(0xFFDC2626),
    onError = Color.White
)

@Composable
fun VoxoraStudioTheme(
    darkTheme: Boolean = true, // Default to Studio Dark Mode
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkStudioColorScheme else LightStudioColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
