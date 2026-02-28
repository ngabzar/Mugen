package com.nihongoquest.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nihongoquest.app.data.model.QuizItem
import com.nihongoquest.app.data.repository.DataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class QuizResult { NONE, CORRECT, WRONG }

data class QuizUiState(
    val questions: List<QuizItem> = emptyList(),
    val currentIndex: Int = 0,
    val userAnswer: String = "",
    val result: QuizResult = QuizResult.NONE,
    val score: Int = 0,
    val isLoading: Boolean = true,
    val isFinished: Boolean = false,
    val showAnswer: Boolean = false
)

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = DataRepository(application)
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState

    fun loadQuiz(level: Int) {
        viewModelScope.launch {
            val questions = repo.loadAllQuizForLevel(level).shuffled().take(20)
            _uiState.value = QuizUiState(questions = questions, isLoading = false)
        }
    }

    fun updateAnswer(answer: String) {
        _uiState.value = _uiState.value.copy(userAnswer = answer)
    }

    fun submitAnswer() {
        val state = _uiState.value
        val current = state.questions.getOrNull(state.currentIndex) ?: return
        val isCorrect = current.correctAnswers.any {
            it.trim().equals(state.userAnswer.trim(), ignoreCase = true)
        }
        val newScore = if (isCorrect) state.score + 1 else state.score
        _uiState.value = state.copy(
            result = if (isCorrect) QuizResult.CORRECT else QuizResult.WRONG,
            score = newScore,
            showAnswer = true
        )
    }

    fun nextQuestion() {
        val state = _uiState.value
        val nextIndex = state.currentIndex + 1
        if (nextIndex >= state.questions.size) {
            _uiState.value = state.copy(isFinished = true)
        } else {
            _uiState.value = state.copy(
                currentIndex = nextIndex,
                userAnswer = "",
                result = QuizResult.NONE,
                showAnswer = false
            )
        }
    }

    fun resetQuiz() {
        val questions = _uiState.value.questions.shuffled()
        _uiState.value = QuizUiState(questions = questions, isLoading = false)
    }
}
