package com.example.ui.screens.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.OnGreenContainer
import com.example.ui.theme.OnRedContainer
import com.example.ui.theme.RedContainer
import com.example.ui.theme.RedError
import com.example.ui.viewmodel.MainViewModel

@Composable
fun McqQuizScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val quizState by viewModel.quizState.collectAsState()

    if (quizState.questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("কোনো প্রশ্ন পাওয়া যায়নি", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onBackClick) {
                    Text("ফিরে যান")
                }
            }
        }
        return
    }

    if (quizState.isSubmitted) {
        // Result Screen
        QuizResultView(
            quizState = quizState,
            onRetry = { viewModel.retryQuiz() },
            onBack = onBackClick
        )
    } else {
        // Active Quiz View
        ActiveQuizView(
            quizState = quizState,
            onOptionSelected = { qIndex, optIndex -> viewModel.selectAnswer(qIndex, optIndex) },
            onNextQuestion = { viewModel.goToQuestion(quizState.currentQuestionIndex + 1) },
            onPreviousQuestion = { viewModel.goToQuestion(quizState.currentQuestionIndex - 1) },
            onJumpToQuestion = { index -> viewModel.goToQuestion(index) },
            onSubmit = { viewModel.submitQuiz() }
        )
    }
}

@Composable
private fun ActiveQuizView(
    quizState: com.example.ui.viewmodel.ActiveQuizState,
    onOptionSelected: (Int, Int) -> Unit,
    onNextQuestion: () -> Unit,
    onPreviousQuestion: () -> Unit,
    onJumpToQuestion: (Int) -> Unit,
    onSubmit: () -> Unit
) {
    val currentQIndex = quizState.currentQuestionIndex
    val currentQuestion = quizState.questions.getOrNull(currentQIndex) ?: return
    val selectedOption = quizState.selectedAnswers[currentQIndex]
    val optionLabels = listOf("ক", "খ", "গ", "ঘ")

    val minutes = quizState.timeRemainingSeconds / 60
    val seconds = quizState.timeRemainingSeconds % 60
    val timerText = String.format("%02d:%02d", minutes, seconds)

    Scaffold(
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onPreviousQuestion,
                        enabled = currentQIndex > 0,
                        modifier = Modifier.testTag("quiz_previous_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("পূর্ববর্তী")
                    }

                    if (currentQIndex < quizState.totalQuestions - 1) {
                        Button(
                            onClick = onNextQuestion,
                            modifier = Modifier.testTag("quiz_next_button")
                        ) {
                            Text("পরবর্তী")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    } else {
                        Button(
                            onClick = onSubmit,
                            colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess),
                            modifier = Modifier.testTag("quiz_submit_button")
                        ) {
                            Text("জমা দিন (ফলাফল)")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("quiz_active_container"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header: Quiz Title & Timer
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = quizState.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "প্রশ্ন ${currentQIndex + 1} / ${quizState.totalQuestions}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Timer Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (quizState.timeRemainingSeconds <= 60) RedContainer else MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (quizState.timeRemainingSeconds <= 60) RedError else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = timerText,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (quizState.timeRemainingSeconds <= 60) RedError else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                // Progress Bar
                LinearProgressIndicator(
                    progress = { (currentQIndex + 1).toFloat() / quizState.totalQuestions.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer
                )
            }

            // Question Navigator Pills
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(quizState.totalQuestions) { idx ->
                        val isAnswered = quizState.selectedAnswers.containsKey(idx)
                        val isCurrent = idx == currentQIndex
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCurrent -> MaterialTheme.colorScheme.primary
                                        isAnswered -> GreenSuccess.copy(alpha = 0.2f)
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                )
                                .border(
                                    width = if (isCurrent) 2.dp else 1.dp,
                                    color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                    shape = CircleShape
                                )
                                .clickable { onJumpToQuestion(idx) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${idx + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Question Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "প্রশ্ন নং ${currentQIndex + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currentQuestion.questionBn,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Options (4 Options)
            itemsIndexed(currentQuestion.options) { optIndex, optionText ->
                val isSelected = selectedOption == optIndex
                val optLabel = optionLabels.getOrElse(optIndex) { "${optIndex + 1}" }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onOptionSelected(currentQIndex, optIndex) }
                        .testTag("option_${currentQIndex}_$optIndex"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        width = if (isSelected) 2.dp else 1.dp,
                        brush = androidx.compose.ui.graphics.SolidColor(
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = optLabel,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizResultView(
    quizState: com.example.ui.viewmodel.ActiveQuizState,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    val percentage = quizState.scorePercentage
    val congratulationText = when {
        percentage >= 80 -> "চমৎকার! আপনি অত্যন্ত ভালো করেছেন! 🎉"
        percentage >= 50 -> "ভালো হয়েছে! নিয়মিত পড়লে আরও উন্নতি হবে। 👍"
        else -> "আরও অনুশীলনের প্রয়োজন! ব্যাখ্যাগুলো মনোযোগ দিয়ে পড়ুন। 📚"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("quiz_result_view"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Result Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (percentage >= 50) GreenContainer.copy(alpha = 0.6f) else RedContainer.copy(alpha = 0.6f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(if (percentage >= 50) GreenSuccess else RedError),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "ফলাফল: $percentage%",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (percentage >= 50) OnGreenContainer else OnRedContainer
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = congratulationText,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ResultStatItem(label = "মোট প্রশ্ন", value = "${quizState.totalQuestions}", color = MaterialTheme.colorScheme.primary)
                        ResultStatItem(label = "সঠিক", value = "${quizState.correctCount}", color = GreenSuccess)
                        ResultStatItem(label = "ভুল", value = "${quizState.wrongCount}", color = RedError)
                        ResultStatItem(label = "অনুত্তরিত", value = "${quizState.skippedCount}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onRetry,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quiz_retry_button")
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("আবার পরীক্ষা দিন")
                }

                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quiz_finish_button")
                ) {
                    Text("হোমে ফিরে যান")
                }
            }
        }

        // Answer Explanations Header
        item {
            Text(
                text = "সকল প্রশ্নের সমাধান ও ব্যাখ্যা",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Questions review list with detailed explanations
        itemsIndexed(quizState.questions) { index, question ->
            val userAnswerIndex = quizState.selectedAnswers[index]
            val isCorrect = userAnswerIndex == question.correctOptionIndex
            val isSkipped = userAnswerIndex == null

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "প্রশ্ন নং ${index + 1}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when {
                                isCorrect -> GreenContainer
                                isSkipped -> MaterialTheme.colorScheme.surfaceVariant
                                else -> RedContainer
                            }
                        ) {
                            Text(
                                text = when {
                                    isCorrect -> "সঠিক ✓"
                                    isSkipped -> "উত্তর দেওয়া হয়নি"
                                    else -> "ভুল ✕"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    isCorrect -> GreenSuccess
                                    isSkipped -> MaterialTheme.colorScheme.onSurfaceVariant
                                    else -> RedError
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = question.questionBn,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // User answer & correct answer
                    val correctText = question.options.getOrElse(question.correctOptionIndex) { "" }
                    val userText = if (userAnswerIndex != null) question.options.getOrElse(userAnswerIndex) { "" } else "কোনোটি নয়"

                    if (!isCorrect) {
                        Text(
                            text = "আপনার উত্তর: $userText",
                            style = MaterialTheme.typography.bodySmall,
                            color = RedError
                        )
                    }
                    Text(
                        text = "সঠিক উত্তর: $correctText",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = GreenSuccess
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Explanation Box
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = AmberAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ব্যাখ্যা: ${question.explanationBn}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultStatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
