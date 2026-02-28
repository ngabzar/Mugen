package com.nihongoquest.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Color Palette (dark Japanese aesthetic)
val Background = Color(0xFF020617)
val Surface = Color(0xFF0F172A)
val SurfaceVariant = Color(0xFF1E293B)
val CardBg = Color(0xFF1A1A2E)
val Primary = Color(0xFFE879F9)       // Purple
val PrimaryVariant = Color(0xFFA855F7)
val Secondary = Color(0xFF38BDF8)     // Sky blue
val Tertiary = Color(0xFF34D399)      // Emerald
val Warning = Color(0xFFFBBF24)       // Amber
val Error = Color(0xFFF87171)
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF475569)
val BorderColor = Color(0xFF1E293B)

// Level colors
val ColorN5 = Color(0xFF22C55E)
val ColorN4 = Color(0xFF3B82F6)
val ColorN3 = Color(0xFFF59E0B)
val ColorN2 = Color(0xFFEF4444)
val ColorN1 = Color(0xFFE879F9)

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
    background = Background,
    surface = Surface,
    surfaceVariant = SurfaceVariant,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = Error,
    onError = Color.White
)

@Composable
fun NihongoQuestTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}

fun levelColor(level: String): Color = when (level.uppercase()) {
    "N5" -> ColorN5
    "N4" -> ColorN4
    "N3" -> ColorN3
    "N2" -> ColorN2
    "N1" -> ColorN1
    else -> Color(0xFF64748B)
}
