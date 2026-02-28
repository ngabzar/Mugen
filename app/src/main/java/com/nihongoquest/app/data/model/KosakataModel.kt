package com.nihongoquest.app.data.model

data class KosakataItem(
    val word: String,
    val reading: String,
    val meaning: String,
    val example_sentence: String? = null,
    val example_meaning: String? = null,
    val category: String? = null
)
