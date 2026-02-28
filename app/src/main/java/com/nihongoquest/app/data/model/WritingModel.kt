package com.nihongoquest.app.data.model

data class StrokeInfo(
    val order: Int,
    val description: String? = null
)

data class WritingItem(
    val char: String,
    val romaji: String,
    val strokes: Int,
    val stroke_order: List<StrokeInfo>? = null,
    val type: String? = null
)
