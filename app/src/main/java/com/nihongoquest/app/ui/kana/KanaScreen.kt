package com.nihongoquest.app.ui.kana

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.nihongoquest.app.viewmodel.KanaViewModel

@Composable
fun KanaScreen(type: String, navController: NavController) {
    val viewModel: KanaViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val filteredItems = viewModel.filteredItems()

    LaunchedEffect(type) { viewModel.loadKana(type) }

    val displayTitle = if (type == "hiragana") "Hiragana" else "Katakana"
    val currentItem = filteredItems.getOrNull(uiState.currentIndex)

    // Flip animation
    val rotation by animateFloatAsState(
        targetValue = if (uiState.isFlipped) 180f else 0f,
        animationSpec = tween(400)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617))
    ) {
        NihongoTopBar(title = displayTitle, onBack = { navController.popBackStack() })

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF7C6AF5))
            }
            return@Column
        }

        // Group filter
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.availableGroups()) { group ->
                FilterChip(
                    selected = uiState.selectedGroup == group,
                    onClick = { viewModel.selectGroup(group) },
                    label = { Text(group.take(10)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF7C6AF5),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Progress
        if (filteredItems.isNotEmpty()) {
            Text(
                text = "${uiState.currentIndex + 1} / ${filteredItems.size}",
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }

        // Flashcard
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            if (currentItem != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.75f)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                        }
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                            )
                        )
                        .clickable { viewModel.flipCard() }
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation <= 90f) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = currentItem.char,
                                fontSize = 100.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = currentItem.group,
                                fontSize = 14.sp,
                                color = Color(0xFF4ECDC4),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Ketuk untuk balik",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.graphicsLayer { rotationY = 180f }
                        ) {
                            Text(
                                text = currentItem.romaji,
                                fontSize = 56.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7C6AF5)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = currentItem.char,
                                fontSize = 32.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            } else {
                Text("Tidak ada data", color = Color.White)
            }
        }

        // Navigation buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { viewModel.prevCard() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier.weight(1f)
            ) {
                Text("← Prev", color = Color.White)
            }
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = { viewModel.nextCard() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C6AF5)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Next →", color = Color.White)
            }
        }
    }
}
