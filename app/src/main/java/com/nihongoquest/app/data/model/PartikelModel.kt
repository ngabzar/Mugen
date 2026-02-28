package com.nihongoquest.app.data.model

data class PartikelExample(
    val japanese: String,
    val indonesian: String
)

data class PartikelItem(
    val particle: String,
    val usage: String,
    val explanation: String,
    val examples: List<PartikelExample>
)
