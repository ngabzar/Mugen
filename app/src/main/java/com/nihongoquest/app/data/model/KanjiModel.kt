package com.nihongoquest.app.data.model

data class KanjiReading(
    val hiragana: String,
    val romaji: String
)

data class KanjiVocabulary(
    val word: String,
    val reading: String,
    val meaning: String
)

data class ExampleSentence(
    val japanese: String,
    val indonesian: String
)

data class KanjiItem(
    val kanji: String,
    val onyomi: KanjiReading,
    val kunyomi: KanjiReading,
    val vocabulary: List<KanjiVocabulary>,
    val example_sentence: ExampleSentence,
    val story: String
)
