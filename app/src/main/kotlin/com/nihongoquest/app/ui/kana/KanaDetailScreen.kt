package com.nihongoquest.app.ui.kana

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nihongoquest.app.data.model.KanaItem
import com.nihongoquest.app.data.model.KanaType
import com.nihongoquest.app.data.model.UiState
import com.nihongoquest.app.ui.LoadingState
import com.nihongoquest.app.ui.NihongoTopBar
import com.nihongoquest.app.ui.theme.*

@Composable
fun KanaDetailScreen(
    kanaType: KanaType,
    onBack: () -> Unit,
    viewModel: KanaViewModel = viewModel()
) {
    LaunchedEffect(kanaType) { viewModel.load(kanaType) }
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        NihongoTopBar(title = kanaType.label, onBack = onBack)

        when (val s = state) {
            is UiState.Loading -> LoadingState()
            is UiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(s.message, color = Error)
                }
            }
            is UiState.Success -> {
                val grouped = s.data.groupBy { it.group }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    grouped.forEach { (group, items) ->
                        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(5) }) {
                            Text(
                                text = formatGroupName(group),
                                color = Secondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                            )
                        }
                        items(items) { kana ->
                            KanaCell(kana = kana, color = if (kanaType == KanaType.HIRAGANA) Secondary else Primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KanaCell(kana: KanaItem, color: androidx.compose.ui.graphics.Color) {
    Column(
        modifier = Modifier
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceVariant)
            .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = kana.char,
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = kana.romaji,
            color = color,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}

private fun formatGroupName(group: String): String = when (group.uppercase()) {
    "GOJUUON" -> "Gojūon (五十音)"
    "DAKUTEN" -> "Dakuten (濁点)"
    "HANDAKUTEN" -> "Handakuten (半濁点)"
    "YOUON" -> "Yōon (拗音)"
    "COMBO" -> "Kombinasi"
    else -> group
}
