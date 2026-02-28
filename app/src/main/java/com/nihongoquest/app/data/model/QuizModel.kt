package com.nihongoquest.app.data.model

data class QuizItem(
    val q: String,
    val inputType: String,
    val correctAnswers: List<String>,
    val type: String,
    val source: String,
    val japanese: String?
)
