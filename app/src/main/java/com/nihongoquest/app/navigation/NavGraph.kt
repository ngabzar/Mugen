package com.nihongoquest.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nihongoquest.app.ui.home.HomeScreen
import com.nihongoquest.app.ui.kana.KanaScreen
import com.nihongoquest.app.ui.kanji.KanjiScreen
import com.nihongoquest.app.ui.quiz.QuizScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Kana : Screen("kana/{type}") {
        fun createRoute(type: String) = "kana/$type"
    }
    object Kanji : Screen("kanji/{level}") {
        fun createRoute(level: Int) = "kanji/$level"
    }
    object Quiz : Screen("quiz/{level}") {
        fun createRoute(level: Int) = "quiz/$level"
    }
}

@Composable
fun NihongoNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.Kana.route,
            arguments = listOf(navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: "hiragana"
            KanaScreen(type = type, navController = navController)
        }
        composable(
            route = Screen.Kanji.route,
            arguments = listOf(navArgument("level") { type = NavType.IntType })
        ) { backStackEntry ->
            val level = backStackEntry.arguments?.getInt("level") ?: 5
            KanjiScreen(level = level, navController = navController)
        }
        composable(
            route = Screen.Quiz.route,
            arguments = listOf(navArgument("level") { type = NavType.IntType })
        ) { backStackEntry ->
            val level = backStackEntry.arguments?.getInt("level") ?: 5
            QuizScreen(level = level, navController = navController)
        }
    }
}
