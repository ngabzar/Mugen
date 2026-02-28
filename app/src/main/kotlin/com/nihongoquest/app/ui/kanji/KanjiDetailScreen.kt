package com.nihongoquest.app.ui.kanji

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nihongoquest.app.data.model.JlptLevel
import com.nihongoquest.app.data.model.KanjiItem
import com.nihongoquest.app.data.model.UiState
import com.nihongoquest.app.ui.LoadingState
import com.nihongoquest.app.ui.NihongoTopBar
import com.nihongoquest.app.ui.theme.*

@Composable
fun KanjiDetailScreen(
    level: JlptLevel,
    onBack: () -> Unit,
    viewModel: KanjiViewModel = viewModel()
) {
    LaunchedEffect(level) { viewModel.load(level) }
    val state by viewModel.state.collectAsState()
    var search by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        NihongoTopBar(title = "Kanji ${level.label}", onBack = onBack)

        when (val s = state) {
            is UiState.Loading -> LoadingState()
            is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(s.message, color = Error)
            }
            is UiState.Success -> {
                val filtered = if (search.isBlank()) s.data
                else s.data.filter {
                    it.kanji.contains(search) ||
                    it.onyomi.romaji.contains(search, ignoreCase = true) ||
                    it.kunyomi.romaji.contains(search, ignoreCase = true)
                }

                Column {
                    // Search bar
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        placeholder = { Text("Cari kanji...", color = TextMuted) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = BorderColor,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = Primary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Text(
                        text = "${filtered.size} kanji",
                        color = TextMuted,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filtered, key = { it.kanji }) { kanji ->
                            KanjiCard(kanji = kanji, levelColor = levelColor(level.label))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KanjiCard(kanji: KanjiItem, levelColor: androidx.compose.ui.graphics.Color) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceVariant)
            .border(1.dp, levelColor.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .clickable { expanded = !expanded }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Big kanji character
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(levelColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(kanji.kanji, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row {
                    InfoChip("On: ${kanji.onyomi.romaji.take(20)}", levelColor)
                }
                Spacer(Modifier.height(4.dp))
                Row {
                    InfoChip("Kun: ${kanji.kunyomi.romaji.take(20)}", Secondary)
                }
                if (kanji.vocabulary.isNotEmpty()) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = kanji.vocabulary.first().meaning,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp).padding(bottom = 16.dp)) {
                Divider(color = BorderColor, thickness = 1.dp)
                Spacer(Modifier.height(12.dp))

                if (kanji.story.isNotBlank()) {
                    Text("📖 Cerita Mnemonik", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                    Text(kanji.story, color = TextPrimary, fontSize = 13.sp, lineHeight = 20.sp)
                    Spacer(Modifier.height(12.dp))
                }

                if (kanji.vocabulary.isNotEmpty()) {
                    Text("📝 Kosakata", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(6.dp))
                    kanji.vocabulary.forEach { vocab ->
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Text("${vocab.word} ", color = levelColor, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("[${vocab.reading}] ", color = TextSecondary, fontSize = 13.sp)
                            Text("= ${vocab.meaning}", color = TextPrimary, fontSize = 13.sp)
                        }
                    }
                }

                if (kanji.exampleSentence.japanese.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    Text("💬 Contoh Kalimat", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                    Text(kanji.exampleSentence.japanese, color = TextPrimary, fontSize = 14.sp, lineHeight = 22.sp)
                    Text(kanji.exampleSentence.indonesian, color = TextMuted, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun InfoChip(text: String, color: androidx.compose.ui.graphics.Color) {
    Text(
        text = text,
        color = color,
        fontSize = 11.sp,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}
