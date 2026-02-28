package com.nihongoquest.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nihongoquest.app.data.model.JlptLevel
import com.nihongoquest.app.data.model.KanaType
import com.nihongoquest.app.data.repository.QuizType
import com.nihongoquest.app.ui.bunpou.BunpouDetailScreen
import com.nihongoquest.app.ui.bunpou.BunpouScreen
import com.nihongoquest.app.ui.home.HomeScreen
import com.nihongoquest.app.ui.kana.KanaDetailScreen
import com.nihongoquest.app.ui.kana.KanaScreen
import com.nihongoquest.app.ui.kanji.KanjiDetailScreen
import com.nihongoquest.app.ui.kanji.KanjiScreen
import com.nihongoquest.app.ui.kosakata.KosakataScreen
import com.nihongoquest.app.ui.quiz.QuizResultScreen
import com.nihongoquest.app.ui.quiz.QuizScreen
import com.nihongoquest.app.ui.quiz.QuizSelectScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Kana : Screen("kana")
    object KanaDetail : Screen("kana/{type}") {
        fun createRoute(type: String) = "kana/$type"
    }
    object Kanji : Screen("kanji")
    object KanjiDetail : Screen("kanji/{level}") {
        fun createRoute(level: String) = "kanji/$level"
    }
    object Bunpou : Screen("bunpou")
    object BunpouDetail : Screen("bunpou/{level}") {
        fun createRoute(level: String) = "bunpou/$level"
    }
    object Kosakata : Screen("kosakata")
    object QuizSelect : Screen("quiz")
    object Quiz : Screen("quiz/{level}/{type}") {
        fun createRoute(level: String, type: String) = "quiz/$level/$type"
    }
    object QuizResult : Screen("quiz_result/{score}/{total}") {
        fun createRoute(score: Int, total: Int) = "quiz_result/$score/$total"
    }
}

@Composable
fun NihongoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(Screen.Kana.route) {
            KanaScreen(
                onKanaTypeSelected = { type ->
                    navController.navigate(Screen.KanaDetail.createRoute(type.folder))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Screen.KanaDetail.route,
            arguments = listOf(navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->
            val typeStr = backStackEntry.arguments?.getString("type") ?: "hiragana"
            val kanaType = if (typeStr == "katakana") KanaType.KATAKANA else KanaType.HIRAGANA
            KanaDetailScreen(
                kanaType = kanaType,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Kanji.route) {
            KanjiScreen(
                onLevelSelected = { level ->
                    navController.navigate(Screen.KanjiDetail.createRoute(level.name))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Screen.KanjiDetail.route,
            arguments = listOf(navArgument("level") { type = NavType.StringType })
        ) { backStackEntry ->
            val levelStr = backStackEntry.arguments?.getString("level") ?: "N5"
            val level = JlptLevel.valueOf(levelStr)
            KanjiDetailScreen(
                level = level,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Bunpou.route) {
            BunpouScreen(
                onLevelSelected = { level ->
                    navController.navigate(Screen.BunpouDetail.createRoute(level.name))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Screen.BunpouDetail.route,
            arguments = listOf(navArgument("level") { type = NavType.StringType })
        ) { backStackEntry ->
            val levelStr = backStackEntry.arguments?.getString("level") ?: "N5"
            val level = JlptLevel.valueOf(levelStr)
            BunpouDetailScreen(
                level = level,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Kosakata.route) {
            KosakataScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.QuizSelect.route) {
            QuizSelectScreen(
                onStartQuiz = { level, type ->
                    navController.navigate(Screen.Quiz.createRoute(level.name, type.name))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Screen.Quiz.route,
            arguments = listOf(
                navArgument("level") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val levelStr = backStackEntry.arguments?.getString("level") ?: "N5"
            val typeStr = backStackEntry.arguments?.getString("type") ?: "JLPT"
            val level = JlptLevel.valueOf(levelStr)
            val quizType = QuizType.valueOf(typeStr)
            QuizScreen(
                level = level,
                quizType = quizType,
                onFinished = { score, total ->
                    navController.navigate(Screen.QuizResult.createRoute(score, total)) {
                        popUpTo(Screen.QuizSelect.route)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Screen.QuizResult.route,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0
            QuizResultScreen(
                score = score,
                total = total,
                onPlayAgain = { navController.navigate(Screen.QuizSelect.route) },
                onHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
