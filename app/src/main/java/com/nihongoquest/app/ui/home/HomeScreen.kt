package com.nihongoquest.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nihongoquest.app.navigation.Screen

data class MenuItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val gradient: List<Color>,
    val onClick: () -> Unit
)

@Composable
fun HomeScreen(navController: NavController) {
    val menuItems = listOf(
        MenuItem(
            title = "ひらがな",
            subtitle = "Hiragana",
            icon = Icons.Default.Language,
            gradient = listOf(Color(0xFF667EEA), Color(0xFF764BA2)),
            onClick = { navController.navigate(Screen.Kana.createRoute("hiragana")) }
        ),
        MenuItem(
            title = "カタカナ",
            subtitle = "Katakana",
            icon = Icons.Default.Translate,
            gradient = listOf(Color(0xFF4ECDC4), Color(0xFF44B0AF)),
            onClick = { navController.navigate(Screen.Kana.createRoute("katakana")) }
        ),
        MenuItem(
            title = "漢字",
            subtitle = "Kanji N5",
            icon = Icons.Default.MenuBook,
            gradient = listOf(Color(0xFFF093FB), Color(0xFFF5576C)),
            onClick = { navController.navigate(Screen.Kanji.createRoute(5)) }
        ),
        MenuItem(
            title = "Kuis",
            subtitle = "JLPT N5",
            icon = Icons.Default.Quiz,
            gradient = listOf(Color(0xFF43E97B), Color(0xFF38F9D7)),
            onClick = { navController.navigate(Screen.Quiz.createRoute(5)) }
        ),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF020617), Color(0xFF0F172A))
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            text = "NihongoQuest",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Belajar Bahasa Jepang",
                            fontSize = 14.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            item {
                // JLPT Levels for Kanji
                Text(
                    text = "Kanji per Level JLPT",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items((5 downTo 1).toList()) { level ->
                        LevelButton(
                            level = "N$level",
                            onClick = { navController.navigate(Screen.Kanji.createRoute(level)) }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            item {
                Text(
                    text = "Kuis JLPT",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items((5 downTo 1).toList()) { level ->
                        LevelButton(
                            level = "N$level",
                            color = Color(0xFF43E97B),
                            onClick = { navController.navigate(Screen.Quiz.createRoute(level)) }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Text(
                    text = "Menu Utama",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }

            items(menuItems) { item ->
                HomeMenuCard(item = item)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun LevelButton(level: String, color: Color = Color(0xFF7C6AF5), onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(level, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun HomeMenuCard(item: MenuItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable(onClick = item.onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(item.gradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = item.subtitle,
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF94A3B8)
            )
        }
    }
}
