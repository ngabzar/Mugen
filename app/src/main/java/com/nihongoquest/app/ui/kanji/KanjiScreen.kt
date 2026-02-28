package com.nihongoquest.app.ui.kanji

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nihongoquest.app.ui.components.NihongoTopBar
import com.nihongoquest.app.viewmodel.KanjiViewModel

@Composable
fun KanjiScreen(level: Int, navController: NavController) {
    val viewModel: KanjiViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val filteredItems = viewModel.filteredItems()

    LaunchedEffect(level) { viewModel.loadKanji(level) }

    val currentItem = filteredItems.getOrNull(uiState.currentIndex)

    val rotation by animateFloatAsState(
        targetValue = if (uiState.isFlipped) 180f else 0f,
        animationSpec = tween(400)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617))
    ) {
        NihongoTopBar(title = "Kanji N$level", onBack = { navController.popBackStack() })

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFF59E0B))
            }
            return@Column
        }

        // Progress
        if (filteredItems.isNotEmpty()) {
            LinearProgressIndicator(
                progress = { (uiState.currentIndex + 1).toFloat() / filteredItems.size },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                color = Color(0xFFF59E0B),
                trackColor = Color(0xFF1E293B)
            )
            Text(
                text = "${uiState.currentIndex + 1} / ${filteredItems.size} kanji",
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Flashcard
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (currentItem != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.65f)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                        }
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.verticalGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A)))
                        )
                        .clickable { viewModel.flipCard() }
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation <= 90f) {
                        // Front - kanji
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = currentItem.kanji,
                                fontSize = 96.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(Modifier.height(16.dp))
                            Surface(
                                color = Color(0xFF1E3A5F),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "N$level",
                                    color = Color(0xFF60A5FA),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text("Ketuk untuk lihat detail", color = Color(0xFF64748B), fontSize = 12.sp)
                        }
                    } else {
                        // Back - details
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .graphicsLayer { rotationY = 180f }
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = currentItem.kanji,
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Divider(color = Color(0xFF334155))
                            Row {
                                Text("音読み: ", color = Color(0xFFF59E0B), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(currentItem.onyomi.hiragana, color = Color.White, fontSize = 13.sp)
                            }
                            Row {
                                Text("訓読み: ", color = Color(0xFF4ECDC4), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(currentItem.kunyomi.hiragana, color = Color.White, fontSize = 13.sp)
                            }
                            Divider(color = Color(0xFF334155))
                            Text("Contoh Kata:", color = Color(0xFF94A3B8), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            currentItem.vocabulary.take(3).forEach { vocab ->
                                Surface(
                                    color = Color(0xFF0F172A),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${vocab.word}（${vocab.reading}）",
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(vocab.meaning, color = Color(0xFF94A3B8), fontSize = 12.sp)
                                    }
                                }
                            }
                            if (currentItem.story.isNotEmpty()) {
                                Divider(color = Color(0xFF334155))
                                Text("💡 ${currentItem.story}", color = Color(0xFFFDE68A), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { viewModel.prevCard() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier.weight(1f)
            ) { Text("← Prev", color = Color.White) }
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = { viewModel.nextCard() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                modifier = Modifier.weight(1f)
            ) { Text("Next →", color = Color(0xFF020617), fontWeight = FontWeight.Bold) }
        }
    }
}
