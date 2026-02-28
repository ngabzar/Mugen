package com.nihongoquest.app.data.model

import com.google.gson.annotations.SerializedName

// ===================== KANA =====================
data class KanaItem(
    val char: String = "",
    val romaji: String = "",
    val type: String = "",
    val group: String = ""
)

// ===================== KANJI =====================
data class KanjiItem(
    val kanji: String = "",
    val onyomi: KanjiReading = KanjiReading(),
    val kunyomi: KanjiReading = KanjiReading(),
    val vocabulary: List<KanjiVocabulary> = emptyList(),
    @SerializedName("example_sentence") val exampleSentence: KanjiExample = KanjiExample(),
    val story: String = "",
    val level: String = ""
)

data class KanjiReading(
    val hiragana: String = "",
    val romaji: String = ""
)

data class KanjiVocabulary(
    val word: String = "",
    val reading: String = "",
    val meaning: String = ""
)

data class KanjiExample(
    val japanese: String = "",
    val indonesian: String = ""
)

// ===================== BUNPOU =====================
data class BunpouItem(
    val title: String = "",
    val formula: String = "",
    val explanation: String = "",
    val example: String = "",
    val level: String = ""
)

// ===================== KOSAKATA =====================
data class KosakataItem(
    val word: String = "",
    val kana: String = "",
    val meaning: String = "",
    val level: String = "",
    val category: String = "",
    val tag: String = "",
    val examples: List<KosakataExample> = emptyList()
)

data class KosakataExample(
    val japanese: String = "",
    val romaji: String = "",
    val meaning: String = ""
)

// ===================== QUIZ =====================
data class QuizItem(
    @SerializedName("q") val question: String = "",
    val kana: String = "",
    val romaji: String = "",
    val options: List<String> = emptyList(),
    val correct: Int = 0,
    val type: String = "",
    @SerializedName("inputType") val inputType: String = "CHOICE",
    val explanation: String = "",
    val section: String = "",
    @SerializedName("type_name") val typeName: String = ""
)

// ===================== UI STATE =====================
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

enum class JlptLevel(val label: String, val folderKey: String) {
    N5("N5", "n5"),
    N4("N4", "n4"),
    N3("N3", "n3"),
    N2("N2", "n2"),
    N1("N1", "n1")
}

enum class KanaType(val label: String, val folder: String) {
    HIRAGANA("Hiragana", "hiragana"),
    KATAKANA("Katakana", "katakana")
}

data class QuizSession(
    val questions: List<QuizItem>,
    val currentIndex: Int = 0,
    val score: Int = 0,
    val answers: List<Int?> = emptyList(),
    val isFinished: Boolean = false
) {
    val currentQuestion get() = questions.getOrNull(currentIndex)
    val progress get() = if (questions.isEmpty()) 0f else currentIndex.toFloat() / questions.size
}
