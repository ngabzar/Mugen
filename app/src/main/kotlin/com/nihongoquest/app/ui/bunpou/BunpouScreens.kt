package com.nihongoquest.app.ui.bunpou

import android.app.Application
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
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nihongoquest.app.data.model.BunpouItem
import com.nihongoquest.app.data.model.JlptLevel
import com.nihongoquest.app.data.model.UiState
import com.nihongoquest.app.data.repository.NihongoRepository
import com.nihongoquest.app.ui.LevelBadge
import com.nihongoquest.app.ui.LevelSelector
import com.nihongoquest.app.ui.LoadingState
import com.nihongoquest.app.ui.NihongoTopBar
import com.nihongoquest.app.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// =================== ViewModel ===================
class BunpouViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = NihongoRepository(app)
    private val _state = MutableStateFlow<UiState<List<BunpouItem>>>(UiState.Loading)
    val state: StateFlow<UiState<List<BunpouItem>>> = _state
    private var currentLevel: JlptLevel? = null

    fun load(level: JlptLevel) {
        if (currentLevel == level && _state.value is UiState.Success) return
        currentLevel = level
        viewModelScope.launch {
            _state.value = UiState.Loading
            _state.value = try {
                UiState.Success(repo.loadBunpou(level))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error")
            }
        }
    }
}

// =================== Level Selection Screen ===================
@Composable
fun BunpouScreen(
    onLevelSelected: (JlptLevel) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Background)
    ) {
        NihongoTopBar(title = "Bunpou (文法)", onBack = onBack)

        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text("Pilih Level", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("Pelajari tata bahasa Jepang per level JLPT", color = TextSecondary, fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))
            }
            items(JlptLevel.entries.reversed()) { level ->
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
                        modifier = Modifier.size(50.dp).clip(RoundedCornerShape(12.dp)).background(color.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) { Text("📝", fontSize = 24.sp) }
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        LevelBadge(level.label)
                        Spacer(Modifier.height(4.dp))
                        Text("Pola tata bahasa ${level.label}", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                    Text("→", color = color, fontSize = 20.sp)
                }
            }
        }
    }
}

// =================== Detail Screen ===================
@Composable
fun BunpouDetailScreen(
    level: JlptLevel,
    onBack: () -> Unit,
    viewModel: BunpouViewModel = viewModel()
) {
    LaunchedEffect(level) { viewModel.load(level) }
    val state by viewModel.state.collectAsState()
    var search by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        NihongoTopBar(title = "Bunpou ${level.label}", onBack = onBack)

        when (val s = state) {
            is UiState.Loading -> LoadingState()
            is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(s.message, color = Error)
            }
            is UiState.Success -> {
                val filtered = if (search.isBlank()) s.data
                else s.data.filter {
                    it.title.contains(search, ignoreCase = true) ||
                    it.explanation.contains(search, ignoreCase = true)
                }

                Column {
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        placeholder = { Text("Cari pola...", color = TextMuted) },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Tertiary,
                            unfocusedBorderColor = BorderColor,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = Tertiary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Text("${filtered.size} pola tata bahasa", color = TextMuted, fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp))

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filtered) { item ->
                            BunpouCard(item = item, color = levelColor(level.label))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BunpouCard(item: BunpouItem, color: androidx.compose.ui.graphics.Color) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceVariant)
            .border(1.dp, color.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .clickable { expanded = !expanded }
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, color = color, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(item.explanation, color = TextSecondary, fontSize = 13.sp, maxLines = if (expanded) Int.MAX_VALUE else 2)
            }
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp)
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp).padding(bottom = 16.dp)) {
                Divider(color = BorderColor)
                Spacer(Modifier.height(12.dp))
                if (item.formula.isNotBlank()) {
                    Text("📐 Formula", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                            .background(color.copy(alpha = 0.08f)).padding(12.dp)
                    ) {
                        Text(item.formula, color = color, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                    Spacer(Modifier.height(12.dp))
                }
                if (item.example.isNotBlank()) {
                    Text("💬 Contoh", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                    Text(item.example, color = TextPrimary, fontSize = 13.sp, lineHeight = 22.sp)
                }
            }
        }
    }
}
