package com.nihongoquest.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nihongoquest.app.data.model.KanjiItem
import com.nihongoquest.app.data.repository.DataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class KanjiUiState(
    val items: List<KanjiItem> = emptyList(),
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val isLoading: Boolean = true,
    val searchQuery: String = ""
)

class KanjiViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = DataRepository(application)

    private val _uiState = MutableStateFlow(KanjiUiState())
    val uiState: StateFlow<KanjiUiState> = _uiState

    fun loadKanji(level: Int) {
        viewModelScope.launch {
            val items = repo.loadKanji(level)
            _uiState.value = KanjiUiState(items = items, isLoading = false)
        }
    }

    fun flipCard() {
        _uiState.value = _uiState.value.copy(isFlipped = !_uiState.value.isFlipped)
    }

    fun nextCard() {
        val filtered = filteredItems()
        if (filtered.isEmpty()) return
        val next = (_uiState.value.currentIndex + 1) % filtered.size
        _uiState.value = _uiState.value.copy(currentIndex = next, isFlipped = false)
    }

    fun prevCard() {
        val filtered = filteredItems()
        if (filtered.isEmpty()) return
        val prev = if (_uiState.value.currentIndex == 0) filtered.size - 1 else _uiState.value.currentIndex - 1
        _uiState.value = _uiState.value.copy(currentIndex = prev, isFlipped = false)
    }

    fun search(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query, currentIndex = 0, isFlipped = false)
    }

    fun filteredItems(): List<KanjiItem> {
        val state = _uiState.value
        if (state.searchQuery.isEmpty()) return state.items
        return state.items.filter {
            it.kanji.contains(state.searchQuery) ||
            it.onyomi.hiragana.contains(state.searchQuery) ||
            it.kunyomi.hiragana.contains(state.searchQuery) ||
            it.vocabulary.any { v -> v.meaning.contains(state.searchQuery, ignoreCase = true) }
        }
    }
}
