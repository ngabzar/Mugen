package com.nihongoquest.app.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nihongoquest.app.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NihongoRepository(private val context: Context) {

    private val gson = Gson()

    private fun readAsset(path: String): String {
        return context.assets.open(path).bufferedReader().use { it.readText() }
    }

    private inline fun <reified T> parseJson(json: String): List<T> {
        val type = object : TypeToken<List<T>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ======================== KANA ========================
    suspend fun loadKana(type: KanaType): List<KanaItem> = withContext(Dispatchers.IO) {
        val folder = "data/kana/${type.folder}"
        val allItems = mutableListOf<KanaItem>()
        try {
            val files = context.assets.list(folder) ?: return@withContext emptyList()
            files.filter { it.endsWith(".json") && it != "manifest.json" }
                .sorted()
                .forEach { fileName ->
                    val json = readAsset("$folder/$fileName")
                    allItems.addAll(parseJson(json))
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        allItems
    }

    // ======================== KANJI ========================
    suspend fun loadKanji(level: JlptLevel): List<KanjiItem> = withContext(Dispatchers.IO) {
        val folder = "data/kanji/kanjin${level.folderKey.removePrefix("n")}"
        val allItems = mutableListOf<KanjiItem>()
        try {
            val files = context.assets.list(folder) ?: return@withContext emptyList()
            files.filter { it.endsWith(".json") && it != "manifest.json" }
                .sorted()
                .forEach { fileName ->
                    val json = readAsset("$folder/$fileName")
                    allItems.addAll(parseJson(json))
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        allItems
    }

    // ======================== BUNPOU ========================
    suspend fun loadBunpou(level: JlptLevel): List<BunpouItem> = withContext(Dispatchers.IO) {
        val folder = "data/bunpou/bunpoun${level.folderKey.removePrefix("n")}"
        val allItems = mutableListOf<BunpouItem>()
        try {
            val files = context.assets.list(folder) ?: return@withContext emptyList()
            files.filter { it.endsWith(".json") && it != "manifest.json" }
                .sorted()
                .forEach { fileName ->
                    val json = readAsset("$folder/$fileName")
                    allItems.addAll(parseJson(json))
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        allItems
    }

    // ======================== KOSAKATA ========================
    suspend fun loadKosakata(level: JlptLevel): List<KosakataItem> = withContext(Dispatchers.IO) {
        val folder = "data/kosakata/kosakatan${level.folderKey.removePrefix("n")}"
        val allItems = mutableListOf<KosakataItem>()
        try {
            val files = context.assets.list(folder) ?: return@withContext emptyList()
            files.filter { it.endsWith(".json") && it != "manifest.json" }
                .sorted()
                .forEach { fileName ->
                    val json = readAsset("$folder/$fileName")
                    allItems.addAll(parseJson(json))
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        allItems
    }

    // ======================== QUIZ ========================
    suspend fun loadQuiz(level: JlptLevel, type: QuizType = QuizType.JLPT): List<QuizItem> =
        withContext(Dispatchers.IO) {
            val allItems = mutableListOf<QuizItem>()
            try {
                val files = context.assets.list("data/soal") ?: return@withContext emptyList()
                val levelNum = level.folderKey.removePrefix("n")
                val prefix = when (type) {
                    QuizType.JLPT -> "jlptn${levelNum}"
                    QuizType.KALIMAT -> "soal_kalimat_n${levelNum}"
                    QuizType.PARTIKEL -> "soal_partikel_n${levelNum}"
                }
                files.filter { it.startsWith(prefix) && it.endsWith(".json") }
                    .sorted()
                    .forEach { fileName ->
                        val json = readAsset("data/soal/$fileName")
                        allItems.addAll(parseJson(json))
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            allItems.shuffled()
        }
}

enum class QuizType(val label: String) {
    JLPT("JLPT"),
    KALIMAT("Kalimat"),
    PARTIKEL("Partikel")
}
