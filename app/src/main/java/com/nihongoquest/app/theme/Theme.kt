package com.nihongoquest.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color(0xFF7C6AF5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3D2F8A),
    secondary = Color(0xFF4ECDC4),
    onSecondary = Color(0xFF002020),
    background = Color(0xFF020617),
    surface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFF1E293B),
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC),
    onSurfaceVariant = Color(0xFF94A3B8),
    tertiary = Color(0xFFF59E0B),
    error = Color(0xFFEF4444)
)

@Composable
fun NihongoQuestTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        content = content
    )
}
