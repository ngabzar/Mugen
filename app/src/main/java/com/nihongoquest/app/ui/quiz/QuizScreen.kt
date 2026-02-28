package com.nihongoquest.app.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nihongoquest.app.ui.components.NihongoTopBar
import com.nihongoquest.app.viewmodel.QuizResult
import com.nihongoquest.app.viewmodel.QuizViewModel

@Composable
fun QuizScreen(level: Int, navController: NavController) {
    val viewModel: QuizViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(level) { viewModel.loadQuiz(level) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617))
    ) {
        NihongoTopBar(title = "Kuis JLPT N$level", onBack = { navController.popBackStack() })

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF43E97B))
            }
            return@Column
        }

        if (uiState.isFinished) {
            QuizFinishedScreen(
                score = uiState.score,
                total = uiState.questions.size,
                onRetry = { viewModel.resetQuiz() },
                onBack = { navController.popBackStack() }
            )
            return@Column
        }

        val current = uiState.questions.getOrNull(uiState.currentIndex) ?: return@Column

        // Progress
        LinearProgressIndicator(
            progress = { (uiState.currentIndex).toFloat() / uiState.questions.size },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(top = 16.dp),
            color = Color(0xFF43E97B),
            trackColor = Color(0xFF1E293B)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${uiState.currentIndex + 1}/${uiState.questions.size}", color = Color(0xFF94A3B8), fontSize = 13.sp)
            Text("Skor: ${uiState.score}", color = Color(0xFF43E97B), fontWeight = FontWeight.Bold)
        }

        // Japanese sentence
        if (!current.japanese.isNullOrEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            ) {
                Text(
                    text = current.japanese,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(20.dp)
                )
            }
        }

        // Question
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
        ) {
            Text(
                text = current.q,
                fontSize = 15.sp,
                color = Color(0xFFCBD5E1),
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(Modifier.weight(1f))

        // Answer input
        Column(modifier = Modifier.padding(24.dp)) {
            OutlinedTextField(
                value = uiState.userAnswer,
                onValueChange = { if (uiState.result == QuizResult.NONE) viewModel.updateAnswer(it) },
                placeholder = { Text("Ketik jawaban Anda...", color = Color(0xFF475569)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF43E97B),
                    unfocusedBorderColor = Color(0xFF334155),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color(0xFF43E97B)
                ),
                enabled = uiState.result == QuizResult.NONE,
                maxLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(12.dp))

            // Result feedback
            if (uiState.showAnswer) {
                val (bgColor, emoji, label) = when (uiState.result) {
                    QuizResult.CORRECT -> Triple(Color(0xFF14532D), "✅", "Benar!")
                    QuizResult.WRONG -> Triple(Color(0xFF7F1D1D), "❌", "Salah!")
                    else -> Triple(Color.Transparent, "", "")
                }
                Surface(
                    color = bgColor,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("$emoji $label", color = Color.White, fontWeight = FontWeight.Bold)
                        if (uiState.result == QuizResult.WRONG) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Jawaban: ${current.correctAnswers.firstOrNull() ?: ""}",
                                color = Color(0xFFFCA5A5),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            if (uiState.result == QuizResult.NONE) {
                Button(
                    onClick = { viewModel.submitAnswer() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43E97B)),
                    shape = RoundedCornerShape(12.dp),
                    enabled = uiState.userAnswer.isNotEmpty()
                ) {
                    Text("Submit", color = Color(0xFF020617), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            } else {
                Button(
                    onClick = { viewModel.nextQuestion() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C6AF5)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Selanjutnya →", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun QuizFinishedScreen(score: Int, total: Int, onRetry: () -> Unit, onBack: () -> Unit) {
    val percentage = if (total > 0) (score * 100 / total) else 0
    val (grade, color) = when {
        percentage >= 90 -> "Sempurna! 🏆" to Color(0xFFFFD700)
        percentage >= 75 -> "Hebat! 🌟" to Color(0xFF43E97B)
        percentage >= 60 -> "Bagus! 👍" to Color(0xFF60A5FA)
        else -> "Coba Lagi! 💪" to Color(0xFFF59E0B)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(grade, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(
                text = "$score / $total",
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text("$percentage%", fontSize = 24.sp, color = color)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43E97B)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ulangi Kuis", color = Color(0xFF020617), fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("Kembali ke Menu")
            }
        }
    }
}
