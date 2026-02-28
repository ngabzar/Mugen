package com.nihongoquest.app.ui.kana

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nihongoquest.app.data.model.KanaItem
import com.nihongoquest.app.data.model.KanaType
import com.nihongoquest.app.data.model.UiState
import com.nihongoquest.app.data.repository.NihongoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class KanaViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = NihongoRepository(app)

    private val _state = MutableStateFlow<UiState<List<KanaItem>>>(UiState.Loading)
    val state: StateFlow<UiState<List<KanaItem>>> = _state

    private var currentType: KanaType? = null

    fun load(type: KanaType) {
        if (currentType == type && _state.value is UiState.Success) return
        currentType = type
        viewModelScope.launch {
            _state.value = UiState.Loading
            _state.value = try {
                UiState.Success(repo.loadKana(type))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error loading kana")
            }
        }
    }
}
