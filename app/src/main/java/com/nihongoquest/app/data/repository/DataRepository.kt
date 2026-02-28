package com.nihongoquest.app.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nihongoquest.app.data.model.*

class DataRepository(private val context: Context) {

    private val gson = Gson()

    private fun readAsset(path: String): String? {
        return try {
            context.assets.open(path).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            null
        }
    }

    private fun listAssets(dir: String): List<String> {
        return try {
            context.assets.list(dir)?.toList() ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun loadHiragana(): List<KanaItem> {
        val result = mutableListOf<KanaItem>()
        val files = listAssets("data/kana/hiragana").filter { it.endsWith(".json") && it != "manifest.json" }.sorted()
        for (file in files) {
            val json = readAsset("data/kana/hiragana/$file") ?: continue
            try {
                val type = object : TypeToken<List<KanaItem>>() {}.type
                result.addAll(gson.fromJson(json, type))
            } catch (e: Exception) { /* skip */ }
        }
        return result
    }

    fun loadKatakana(): List<KanaItem> {
        val result = mutableListOf<KanaItem>()
        val files = listAssets("data/kana/katakana").filter { it.endsWith(".json") && it != "manifest.json" }.sorted()
        for (file in files) {
            val json = readAsset("data/kana/katakana/$file") ?: continue
            try {
                val type = object : TypeToken<List<KanaItem>>() {}.type
                result.addAll(gson.fromJson(json, type))
            } catch (e: Exception) { /* skip */ }
        }
        return result
    }

    fun loadKanji(level: Int): List<KanjiItem> {
        val result = mutableListOf<KanjiItem>()
        val dir = "data/kanji/kanjin$level"
        val files = listAssets(dir).filter { it.endsWith(".json") && it != "manifest.json" }.sorted()
        for (file in files) {
            val json = readAsset("$dir/$file") ?: continue
            try {
                val type = object : TypeToken<List<KanjiItem>>() {}.type
                result.addAll(gson.fromJson(json, type))
            } catch (e: Exception) { /* skip */ }
        }
        return result
    }

    fun loadQuiz(filename: String): List<QuizItem> {
        val json = readAsset("data/soal/$filename") ?: return emptyList()
        return try {
            val type = object : TypeToken<List<QuizItem>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun loadAllQuizForLevel(level: Int): List<QuizItem> {
        val result = mutableListOf<QuizItem>()
        listOf("soal_kalimat_n$level.json", "soal_partikel_n$level.json").forEach { file ->
            result.addAll(loadQuiz(file))
        }
        return result
    }

    fun loadWriting(): List<WritingItem> {
        val result = mutableListOf<WritingItem>()
        val files = listAssets("data/writing").filter { it.endsWith(".json") }.sorted()
        for (file in files) {
            val json = readAsset("data/writing/$file") ?: continue
            try {
                val type = object : TypeToken<List<WritingItem>>() {}.type
                result.addAll(gson.fromJson(json, type))
            } catch (e: Exception) { /* skip */ }
        }
        return result
    }

    fun loadKosakata(level: Int): List<KosakataItem> {
        val result = mutableListOf<KosakataItem>()
        val dir = "data/kosakata/kosakatan$level"
        val files = listAssets(dir).filter { it.endsWith(".json") && it != "manifest.json" }.sorted()
        for (file in files) {
            val json = readAsset("$dir/$file") ?: continue
            try {
                val type = object : TypeToken<List<KosakataItem>>() {}.type
                result.addAll(gson.fromJson(json, type))
            } catch (e: Exception) { /* skip */ }
        }
        return result
    }

    fun loadPartikel(level: Int): List<PartikelItem> {
        val result = mutableListOf<PartikelItem>()
        val dir = "data/partikel/partikeln$level"
        val files = listAssets(dir).filter { it.endsWith(".json") && it != "manifest.json" }.sorted()
        for (file in files) {
            val json = readAsset("$dir/$file") ?: continue
            try {
                val type = object : TypeToken<List<PartikelItem>>() {}.type
                result.addAll(gson.fromJson(json, type))
            } catch (e: Exception) { /* skip */ }
        }
        return result
    }
}
