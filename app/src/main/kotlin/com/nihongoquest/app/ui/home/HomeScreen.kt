package com.nihongoquest.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nihongoquest.app.navigation.Screen
import com.nihongoquest.app.ui.FeatureCard
import com.nihongoquest.app.ui.theme.*

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .padding(horizontal = 20.dp, vertical = 28.dp)
        ) {
            Text(
                text = "日本語Quest",
                color = Primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Belajar Bahasa Jepang Lengkap",
                color = TextSecondary,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "✨ Mulai perjalanan belajarmu sekarang!",
                color = Secondary,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(24.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Materi Belajar",
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureCard(
                    icon = "あ",
                    title = "Kana",
                    subtitle = "Hiragana & Katakana\n46+ karakter",
                    color = Secondary,
                    onClick = { onNavigate(Screen.Kana.route) },
                    modifier = Modifier.weight(1f)
                )
                FeatureCard(
                    icon = "漢",
                    title = "Kanji",
                    subtitle = "N5 ~ N1\n2000+ kanji",
                    color = Warning,
                    onClick = { onNavigate(Screen.Kanji.route) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureCard(
                    icon = "📝",
                    title = "Bunpou",
                    subtitle = "Tata Bahasa\nN5 ~ N1",
                    color = Tertiary,
                    onClick = { onNavigate(Screen.Bunpou.route) },
                    modifier = Modifier.weight(1f)
                )
                FeatureCard(
                    icon = "📚",
                    title = "Kosakata",
                    subtitle = "Kosakata\nN5 ~ N1",
                    color = Primary,
                    onClick = { onNavigate(Screen.Kosakata.route) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Latihan",
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            FeatureCard(
                icon = "🎯",
                title = "Kuis JLPT",
                subtitle = "Latihan soal N5 ~ N1 • Kalimat • Partikel",
                color = Error,
                onClick = { onNavigate(Screen.QuizSelect.route) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // Info box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Primary.copy(alpha = 0.08f))
                    .padding(16.dp)
            ) {
                Text("💡 Tips Belajar", color = Primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Mulai dari Hiragana → Katakana → Kosakata N5 → Kanji N5 → Bunpou N5 → Kuis N5. Konsisten setiap hari 30 menit lebih efektif dari belajar sekaligus!",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
