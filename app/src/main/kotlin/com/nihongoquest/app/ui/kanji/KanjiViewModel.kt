package com.nihongoquest.app.ui.kanji

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nihongoquest.app.data.model.JlptLevel
import com.nihongoquest.app.data.model.KanjiItem
import com.nihongoquest.app.data.model.UiState
import com.nihongoquest.app.data.repository.NihongoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class KanjiViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = NihongoRepository(app)

    private val _state = MutableStateFlow<UiState<List<KanjiItem>>>(UiState.Loading)
    val state: StateFlow<UiState<List<KanjiItem>>> = _state

    private var currentLevel: JlptLevel? = null

    fun load(level: JlptLevel) {
        if (currentLevel == level && _state.value is UiState.Success) return
        currentLevel = level
        viewModelScope.launch {
            _state.value = UiState.Loading
            _state.value = try {
                UiState.Success(repo.loadKanji(level))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error")
            }
        }
    }
}
