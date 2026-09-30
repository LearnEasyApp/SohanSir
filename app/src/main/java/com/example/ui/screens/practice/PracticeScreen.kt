package com.example.ui.screens.practice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MCQQuestion
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.RedContainer
import com.example.ui.theme.RedError
import com.example.ui.viewmodel.MainViewModel

@Composable
fun PracticeScreen(
    viewModel: MainViewModel,
    onStartPracticeQuiz: (String) -> Unit
) {
    val subjects = viewModel.getSubjects()
    val bookmarks by viewModel.bookmarks.collectAsState()

    var selectedSubjectId by remember { mutableStateOf("ALL") }
    val userSelectedAnswers = remember { mutableStateMapOf<String, Int>() }
    val revealedExplanations = remember { mutableStateMapOf<String, Boolean>() }

    val allQuestions: List<MCQQuestion> = remember(selectedSubjectId) {
        if (selectedSubjectId == "ALL") {
            viewModel.getAllQuestions()
        } else {
            viewModel.getQuestionsForSubject(selectedSubjectId)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("practice_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "বিষয়ভিত্তিক প্রশ্ন অনুশীলন",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "যেকোনো প্রশ্নের অপশনে ট্যাপ করে তাৎক্ষণিক সঠিক উত্তর ও বিস্তারিত ব্যাখ্যা দেখুন",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Subject Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedSubjectId == "ALL",
                        onClick = { selectedSubjectId = "ALL" },
                        label = { Text("সকল বিষয়") },
                        modifier = Modifier.testTag("filter_all_subjects")
                    )
                }
                items(subjects) { subj ->
                    FilterChip(
                        selected = selectedSubjectId == subj.id,
                        onClick = { selectedSubjectId = subj.id },
                        label = { Text(subj.titleBn) },
                        modifier = Modifier.testTag("filter_subject_${subj.id}")
                    )
                }
            }
        }

        // Action banner to launch timed practice quiz
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "সময় মেপে অনুশীলন করবেন?",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "১০ মিনিটের কুইজে অংশগ্রহণ করে স্কোর যাচাই করুন",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    Button(
                        onClick = {
                            val targetSubj = if (selectedSubjectId == "ALL") "MATH" else selectedSubjectId
                            onStartPracticeQuiz(targetSubj)
                        },
                        modifier = Modifier.testTag("start_timed_practice_button")
                    ) {
                        Text("কুইজ শুরু")
                    }
                }
            }
        }

        // Question cards
        itemsIndexed(allQuestions) { index, question ->
            val selectedOption = userSelectedAnswers[question.id]
            val isExplanationVisible = revealedExplanations[question.id] ?: false
            val isQuestionBookmarked = bookmarks.any { it.id == "QUESTION_${question.id}" }

            PracticeQuestionItem(
                index = index,
                question = question,
                selectedOption = selectedOption,
                isExplanationVisible = isExplanationVisible,
                isBookmarked = isQuestionBookmarked,
                onSelectOption = { optIndex ->
                    userSelectedAnswers[question.id] = optIndex
                    revealedExplanations[question.id] = true
                },
                onToggleExplanation = {
                    revealedExplanations[question.id] = !isExplanationVisible
                },
                onToggleBookmark = {
                    viewModel.toggleBookmark(
                        id = "QUESTION_${question.id}",
                        type = "QUESTION",
                        title = question.questionBn,
                        subtitle = "সঠিক উত্তর: ${question.options[question.correctOptionIndex]}",
                        targetId = question.id,
                        subjectId = question.subjectId
                    )
                }
            )
        }
    }
}

@Composable
private fun PracticeQuestionItem(
    index: Int,
    question: MCQQuestion,
    selectedOption: Int?,
    isExplanationVisible: Boolean,
    isBookmarked: Boolean,
    onSelectOption: (Int) -> Unit,
    onToggleExplanation: () -> Unit,
    onToggleBookmark: () -> Unit
) {
    val isAnswered = selectedOption != null
    val optionLabels = listOf("ক", "খ", "গ", "ঘ")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .testTag("practice_question_card_${question.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Tag & Difficulty & Bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "প্রশ্ন ${index + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = question.difficulty,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "বুকমার্ক",
                        tint = if (isBookmarked) AmberAccent else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = question.questionBn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4 Options
            question.options.forEachIndexed { optIndex, optText ->
                val isSelected = selectedOption == optIndex
                val isCorrect = optIndex == question.correctOptionIndex

                val backgroundColor = when {
                    !isAnswered -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    isCorrect -> GreenContainer
                    isSelected && !isCorrect -> RedContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                }

                val textColor = when {
                    !isAnswered -> MaterialTheme.colorScheme.onSurface
                    isCorrect -> GreenSuccess
                    isSelected && !isCorrect -> RedError
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onSelectOption(optIndex) },
                    color = backgroundColor
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        !isAnswered -> MaterialTheme.colorScheme.surfaceVariant
                                        isCorrect -> GreenSuccess
                                        isSelected && !isCorrect -> RedError
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = optionLabels.getOrElse(optIndex) { "${optIndex + 1}" },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isAnswered && (isCorrect || isSelected)) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = optText,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isAnswered && isCorrect) FontWeight.Bold else FontWeight.Normal,
                            color = textColor,
                            modifier = Modifier.weight(1f)
                        )
                        if (isAnswered && isCorrect) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "সঠিক",
                                tint = GreenSuccess,
                                modifier = Modifier.size(18.dp)
                            )
                        } else if (isAnswered && isSelected && !isCorrect) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "ভুল",
                                tint = RedError,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Explanation Toggle & Box
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = if (isExplanationVisible) "ব্যাখ্যা লুকান" else "ব্যাখ্যা দেখুন",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { onToggleExplanation() }
                        .padding(4.dp)
                )
            }

            AnimatedVisibility(visible = isExplanationVisible) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
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
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
