package com.nihongoquest.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nihongoquest.app.data.model.KanaItem
import com.nihongoquest.app.data.repository.DataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class KanaUiState(
    val items: List<KanaItem> = emptyList(),
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val isLoading: Boolean = true,
    val selectedGroup: String = "ALL"
)

class KanaViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = DataRepository(application)

    private val _uiState = MutableStateFlow(KanaUiState())
    val uiState: StateFlow<KanaUiState> = _uiState

    fun loadKana(type: String) {
        viewModelScope.launch {
            val items = if (type == "hiragana") repo.loadHiragana() else repo.loadKatakana()
            _uiState.value = KanaUiState(items = items, isLoading = false)
        }
    }

    fun flipCard() {
        _uiState.value = _uiState.value.copy(isFlipped = !_uiState.value.isFlipped)
    }

    fun nextCard() {
        val state = _uiState.value
        val items = filteredItems()
        if (items.isEmpty()) return
        val nextIndex = (state.currentIndex + 1) % items.size
        _uiState.value = state.copy(currentIndex = nextIndex, isFlipped = false)
    }

    fun prevCard() {
        val state = _uiState.value
        val items = filteredItems()
        if (items.isEmpty()) return
        val prevIndex = if (state.currentIndex == 0) items.size - 1 else state.currentIndex - 1
        _uiState.value = state.copy(currentIndex = prevIndex, isFlipped = false)
    }

    fun selectGroup(group: String) {
        _uiState.value = _uiState.value.copy(selectedGroup = group, currentIndex = 0, isFlipped = false)
    }

    fun filteredItems(): List<KanaItem> {
        val state = _uiState.value
        return if (state.selectedGroup == "ALL") state.items
        else state.items.filter { it.group == state.selectedGroup }
    }

    fun availableGroups(): List<String> {
        return listOf("ALL") + _uiState.value.items.map { it.group }.distinct()
    }
}
