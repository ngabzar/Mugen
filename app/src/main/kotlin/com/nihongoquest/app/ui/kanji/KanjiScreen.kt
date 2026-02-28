package com.nihongoquest.app.ui.kanji

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nihongoquest.app.data.model.JlptLevel
import com.nihongoquest.app.ui.LevelBadge
import com.nihongoquest.app.ui.NihongoTopBar
import com.nihongoquest.app.ui.theme.*

private val levelInfo = mapOf(
    JlptLevel.N5 to Triple("漢", "80 kanji dasar", "Kanji paling dasar untuk pemula"),
    JlptLevel.N4 to Triple("字", "166 kanji", "Kanji umum sehari-hari"),
    JlptLevel.N3 to Triple("語", "367 kanji", "Kanji tingkat menengah"),
    JlptLevel.N2 to Triple("文", "367 kanji", "Kanji tingkat lanjut"),
    JlptLevel.N1 to Triple("漢", "1000+ kanji", "Kanji tingkat mahir")
)

@Composable
fun KanjiScreen(
    onLevelSelected: (JlptLevel) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        NihongoTopBar(title = "Kanji", onBack = onBack)

        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text("Pilih Level JLPT", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("Pelajari kanji berdasarkan level JLPT", color = TextSecondary, fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))
            }

            items(JlptLevel.entries.reversed()) { level ->
                val (icon, count, desc) = levelInfo[level] ?: Triple("漢", "", "")
                val color = levelColor(level.label)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceVariant)
                        .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .clickable { onLevelSelected(level) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(color.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(icon, fontSize = 28.sp)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LevelBadge(level.label)
                            Spacer(Modifier.width(8.dp))
                            Text(count, color = TextSecondary, fontSize = 12.sp)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(desc, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                    Text("→", color = color, fontSize = 20.sp)
                }
            }
        }
    }
}
