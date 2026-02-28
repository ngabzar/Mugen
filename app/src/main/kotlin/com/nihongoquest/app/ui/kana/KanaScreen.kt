package com.nihongoquest.app.ui.kana

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nihongoquest.app.data.model.KanaType
import com.nihongoquest.app.ui.NihongoTopBar
import com.nihongoquest.app.ui.theme.*

@Composable
fun KanaScreen(
    onKanaTypeSelected: (KanaType) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        NihongoTopBar(title = "Kana", onBack = onBack)

        Spacer(Modifier.height(24.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Pilih Jenis Kana",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Pelajari aksara dasar bahasa Jepang",
                color = TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            KanaTypeCard(
                type = KanaType.HIRAGANA,
                description = "Aksara dasar Jepang untuk kata-kata asli Jepang",
                sample = "あいうえお",
                color = Secondary,
                onClick = { onKanaTypeSelected(KanaType.HIRAGANA) }
            )
            Spacer(Modifier.height(16.dp))
            KanaTypeCard(
                type = KanaType.KATAKANA,
                description = "Aksara untuk kata-kata serapan asing",
                sample = "アイウエオ",
                color = Primary,
                onClick = { onKanaTypeSelected(KanaType.KATAKANA) }
            )
        }
    }
}

@Composable
private fun KanaTypeCard(
    type: KanaType,
    description: String,
    sample: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceVariant)
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = type.label,
                color = color,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = description,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = sample,
                color = TextPrimary,
                fontSize = 20.sp,
                letterSpacing = 4.sp
            )
        }
        Text(
            text = "→",
            color = color,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
