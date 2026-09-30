package com.example.ui.screens.home

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ClassLevel
import com.example.data.model.Subject
import com.example.data.model.WeakTopicSummary
import com.example.ui.components.ProgressBarWithText
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.GreenSuccess
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToSubjects: () -> Unit,
    onNavigateToSubjectDetail: (String) -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToDailyQuiz: () -> Unit,
    onNavigateToModelTest: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToBookmarks: () -> Unit,
    onNavigateToCourses: () -> Unit,
    onNavigateToDownloads: () -> Unit,
    onNavigateToLesson: (String) -> Unit,
    onNavigateToStudyGroups: () -> Unit = {},
    onNavigateToStudyGroupChat: (String) -> Unit = {}
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val completedLessons by viewModel.completedLessons.collectAsState()
    val courses by viewModel.courses.collectAsState()
    val studyGroups by viewModel.studyGroups.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val subjects = viewModel.getSubjects()
    val weakTopics = viewModel.getWeakTopics()

    val currentClass = if (userProfile?.selectedClass == ClassLevel.CLASS_10.id) {
        ClassLevel.CLASS_10
    } else {
        ClassLevel.CLASS_9
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_scroll"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Personalized Greeting & Hero Study Banner: "পড়াশোনা হোক সহজ"
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .testTag("home_hero_banner"),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(155.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.banner_study_hero),
                            contentDescription = "সহজে শিখি ব্যানার",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        // Gradient overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color(0xDD0D47A1))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "শুভদিন, ${userProfile?.studentName ?: "শিক্ষার্থী"}!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AmberAccent
                            )
                            Text(
                                text = "পড়াশোনা হোক সহজ ও আনন্দদায়ক",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "সহজে শিখি, আত্মবিশ্বাসে এগিয়ে যাই • ${currentClass.titleBn}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }

        // 2. Continue Learning Card (সর্বশেষ পঠিত পাঠ ও কোর্স অগ্রগতি)
        item {
            val lastLessonTitle = userProfile?.lastLessonTitle ?: "বাস্তব সংখ্যার শ্রেণিবিভাগ"
            val lastLessonId = userProfile?.lastLessonId ?: "math_ch1_les1"
            val isLessonDone = completedLessons.any { it.lessonId == lastLessonId }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigateToLesson(lastLessonId) }
                    .testTag("continue_learning_card"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isLessonDone) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "পড়াশোনা চালিয়ে যান",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "৩৫% সম্পন্ন",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = lastLessonTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (isLessonDone) "সম্পন্ন হয়েছে • পুনরাবৃত্তি করতে ট্যাপ করুন" else "পাঠটি শুরু করতে ট্যাপ করুন",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "পরবর্তী",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // 3. Quick Action Grid (বিষয়সমূহ, কোর্স, অনুশীলন, দৈনিক কুইজ, মডেল টেস্ট, ডাউনলোড)
        item {
            Text(
                text = "দ্রুত শুরু করুন",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionCard(
                        title = "অনলাইন কোর্স",
                        subtitle = "${courses.size}টি ব্যাচ ও লেকচার",
                        icon = Icons.Default.School,
                        color = Color(0xFF2563EB),
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_courses",
                        onClick = onNavigateToCourses
                    )
                    QuickActionCard(
                        title = "বিষয়সমূহ",
                        subtitle = "৬টি প্রধান বিষয়",
                        icon = Icons.Default.MenuBook,
                        color = Color(0xFF0284C7),
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_subjects",
                        onClick = onNavigateToSubjects
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionCard(
                        title = "অনুশীলন",
                        subtitle = "টপিক ও প্রশ্নব্যাংক",
                        icon = Icons.Default.Psychology,
                        color = Color(0xFF059669),
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_practice",
                        onClick = onNavigateToPractice
                    )
                    QuickActionCard(
                        title = "দৈনিক কুইজ",
                        subtitle = "আজকের চ্যালেঞ্জ",
                        icon = Icons.Default.FlashOn,
                        color = AmberAccent,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_quiz",
                        onClick = onNavigateToDailyQuiz
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionCard(
                        title = "মডেল টেস্ট",
                        subtitle = "এসএসসি চূড়ান্ত পরীক্ষা",
                        icon = Icons.Default.Assignment,
                        color = Color(0xFF7C3AED),
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_model_test",
                        onClick = onNavigateToModelTest
                    )
                    QuickActionCard(
                        title = "অফলাইন নোট",
                        subtitle = "ডাউনলোডকৃত রিসোর্স",
                        icon = Icons.Default.DownloadDone,
                        color = Color(0xFFE11D48),
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_downloads",
                        onClick = onNavigateToDownloads
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionCard(
                        title = "স্টাডি গ্রুপ ও চ্যাট",
                        subtitle = "সহপাঠী ও শিক্ষকের সাথে আলোচনা",
                        icon = Icons.Default.Forum,
                        color = Color(0xFF0D9488),
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_study_groups",
                        onClick = onNavigateToStudyGroups
                    )
                    QuickActionCard(
                        title = "বুকমার্ক সংগ্রহ",
                        subtitle = "সংরক্ষিত সূত্র ও প্রশ্নব্যাংক",
                        icon = Icons.Default.Bookmark,
                        color = Color(0xFFD97706),
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_bookmarks",
                        onClick = onNavigateToBookmarks
                    )
                }
            }
        }

        // 3.5 Active Study Groups Section (স্টাডি গ্রুপ ও শিক্ষা চ্যাট)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "স্টাডি গ্রুপ ও শিক্ষা চ্যাট",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF0D9488).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "লাইভ আলোচনা",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF0D9488),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = "সবগুলো দেখুন",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { onNavigateToStudyGroups() }
                        .testTag("see_all_study_groups")
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                items(studyGroups.take(4)) { group ->
                    Card(
                        modifier = Modifier
                            .width(260.dp)
                            .clickable { onNavigateToStudyGroupChat(group.id) }
                            .testTag("home_group_card_${group.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(group.groupColorHex).copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = group.subjectTitleBn,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(group.groupColorHex),
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (group.unreadCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFDC2626)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = group.unreadCount.toString(),
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = group.nameBn,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = "শিক্ষক: ${group.teacherName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 11.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (group.lastMessageText.isNotEmpty()) group.lastMessageText else group.descriptionBn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${group.memberCount} জন সহপাঠী",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "চ্যাটে যান →",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. My Courses Overview (আমার কোর্সসমূহ)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "আমার কোর্সসমূহ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "সবগুলো দেখুন",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onNavigateToCourses() }
                        .testTag("view_all_courses_link")
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(courses.take(3)) { course ->
                    val progress = viewModel.getCourseProgressPercent(course)

                    Card(
                        modifier = Modifier
                            .width(260.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                val firstLesId = course.lessonIds.firstOrNull() ?: "math_ch1_les1"
                                onNavigateToLesson(firstLesId)
                            }
                            .testTag("home_course_item_${course.id}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = course.difficultyBn,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = course.titleBn,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "শিক্ষক: ${course.instructorName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = { progress / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$progress% সম্পন্ন",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 5. Weak Topics & Revision Recommendations
        if (weakTopics.isNotEmpty()) {
            item {
                Text(
                    text = "দুর্বল টপিক রিভিশন ও সুপারিশ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                val topWeak = weakTopics.first()
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { viewModel.startWeakTopicQuiz(topWeak) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(AmberAccent.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = AmberAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${topWeak.subjectTitleBn}: ${topWeak.topicName}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = topWeak.recommendationBn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.startWeakTopicQuiz(topWeak)
                                onNavigateToPractice()
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("রিভিশন", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // 6. Daily Quiz Challenge Banner (দৈনিক কুইজ চ্যালেঞ্জ)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigateToDailyQuiz() }
                    .testTag("daily_quiz_challenge_card"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(AmberAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "দৈনিক কুইজ চ্যালেঞ্জ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "আজকের ৫টি গুরুত্বপূর্ণ প্রশ্নে নিজেকে যাচাই করুন",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                        )
                    }

                    Button(
                        onClick = onNavigateToDailyQuiz,
                        modifier = Modifier.testTag("start_daily_quiz_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("শুরু করুন", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        // 7. Subject Progress Overview
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "বিষয়সমূহ (${subjects.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "সব দেখুন",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onNavigateToSubjects() }
                        .testTag("view_all_subjects_button")
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // List of subjects with progress
        items(subjects) { subject ->
            val (completed, total) = viewModel.getSubjectProgress(subject.id)
            val subjectIcon = when (subject.id) {
                "MATH" -> Icons.Default.Calculate
                "PHYSICS" -> Icons.Default.Science
                "CHEMISTRY" -> Icons.Default.Biotech
                "BIOLOGY" -> Icons.Default.Psychology
                "ENGLISH" -> Icons.Default.Translate
                else -> Icons.Default.Computer
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigateToSubjectDetail(subject.id) }
                    .testTag("subject_card_${subject.id}"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(subject.colorHex).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = subjectIcon,
                                contentDescription = subject.titleBn,
                                tint = Color(subject.colorHex),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = subject.titleBn,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${subject.chapters.size} টি অধ্যায় • ${subject.chapters.sumOf { it.lessons.size }} টি পাঠ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "অধ্যায় দেখুন",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ProgressBarWithText(
                        current = completed,
                        total = total,
                        label = "পাঠ সম্পন্ন"
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
