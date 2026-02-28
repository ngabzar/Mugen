package com.nihongoquest.app.data.model

data class BunpouExample(
    val japanese: String,
    val indonesian: String
)

data class BunpouItem(
    val pattern: String,
    val meaning: String,
    val explanation: String,
    val examples: List<BunpouExample>,
    val level: String? = null
)
