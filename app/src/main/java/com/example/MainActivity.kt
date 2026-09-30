package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.ClassLevel
import com.example.ui.components.ClassSelectionDialog
import com.example.ui.components.LearnEasyTopBar
import com.example.ui.components.NotificationsDialog
import com.example.ui.screens.admin.AdminCmsScreen
import com.example.ui.screens.bookmark.BookmarkScreen
import com.example.ui.screens.chat.GroupModerationScreen
import com.example.ui.screens.chat.StudyGroupChatScreen
import com.example.ui.screens.chat.StudyGroupsScreen
import com.example.ui.screens.courses.CoursesScreen
import com.example.ui.screens.downloads.DownloadsScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.lesson.LessonDetailScreen
import com.example.ui.screens.modeltest.ModelTestScreen
import com.example.ui.screens.practice.PracticeScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.progress.ProgressScreen
import com.example.ui.screens.quiz.McqQuizScreen
import com.example.ui.screens.search.SearchScreen
import com.example.ui.screens.subjects.ChapterListScreen
import com.example.ui.screens.subjects.SubjectsScreen
import com.example.ui.theme.LearnEasyTheme
import com.example.ui.viewmodel.MainViewModel

sealed class Screen {
    data object Home : Screen()
    data object Courses : Screen()
    data object Subjects : Screen()
    data object Practice : Screen()
    data object ModelTest : Screen()
    data object Progress : Screen()
    data object Profile : Screen()
    data object Bookmarks : Screen()
    data object Downloads : Screen()
    data object Search : Screen()
    data object AdminCms : Screen()
    data object StudyGroups : Screen()
    data class StudyGroupChat(val groupId: String) : Screen()
    data class GroupModeration(val groupId: String = "all") : Screen()
    data class ChapterList(val subjectId: String) : Screen()
    data class LessonDetail(val lessonId: String) : Screen()
    data object McqQuiz : Screen()
}

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val userProfile by viewModel.userProfile.collectAsState()
            val isDarkMode = userProfile?.isDarkMode ?: false

            LearnEasyTheme(darkTheme = isDarkMode) {
                MainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainApp(viewModel: MainViewModel) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isDarkMode = userProfile?.isDarkMode ?: false
    val notifications by viewModel.notifications.collectAsState()
    val unreadNotifsCount by viewModel.unreadNotificationCount.collectAsState()

    val currentClass = ClassLevel.fromId(userProfile?.selectedClass ?: "CLASS_9")

    var showClassDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    // Screen backstack navigation
    val navigationStack = remember { mutableStateListOf<Screen>(Screen.Home) }
    val currentScreen = navigationStack.lastOrNull() ?: Screen.Home

    fun navigateTo(screen: Screen) {
        if (currentScreen != screen) {
            navigationStack.add(screen)
        }
    }

    fun navigateBack() {
        if (navigationStack.size > 1) {
            navigationStack.removeAt(navigationStack.lastIndex)
        } else if (currentScreen !is Screen.Home) {
            navigationStack.clear()
            navigationStack.add(Screen.Home)
        }
    }

    fun switchBottomTab(screen: Screen) {
        navigationStack.clear()
        navigationStack.add(screen)
    }

    // Handle system Back button
    BackHandler(enabled = navigationStack.size > 1 || currentScreen !is Screen.Home) {
        navigateBack()
    }

    // Class selection dialog
    if (showClassDialog) {
        ClassSelectionDialog(
            currentClass = currentClass,
            onDismiss = { showClassDialog = false },
            onClassSelected = { level ->
                viewModel.switchClass(level)
            }
        )
    }

    // Notifications Dialog
    if (showNotificationsDialog) {
        NotificationsDialog(
            notifications = notifications,
            onDismiss = { showNotificationsDialog = false },
            onMarkAsRead = { id -> viewModel.markNotificationAsRead(id) },
            onMarkAllAsRead = { viewModel.markAllNotificationsAsRead() },
            onNotificationClick = { notif ->
                when {
                    notif.targetRoute.startsWith("study_group_") -> {
                        val groupId = notif.targetRoute.removePrefix("study_group_")
                        navigateTo(Screen.StudyGroupChat(groupId))
                    }
                    notif.targetRoute == "mcq_quiz" -> {
                        viewModel.startDailyQuiz()
                        navigateTo(Screen.McqQuiz)
                    }
                    notif.targetRoute == "courses" -> switchBottomTab(Screen.Courses)
                    notif.targetRoute == "progress" -> switchBottomTab(Screen.Progress)
                    else -> {}
                }
            }
        )
    }

    val isTopLevelScreen = currentScreen is Screen.Home ||
            currentScreen is Screen.Courses ||
            currentScreen is Screen.Subjects ||
            currentScreen is Screen.Practice ||
            currentScreen is Screen.Progress ||
            currentScreen is Screen.Profile

    val screenTitle = when (currentScreen) {
        is Screen.Home -> "Learn Easy"
        is Screen.Courses -> "অনলাইন কোর্স"
        is Screen.Subjects -> "বিষয়সমূহ"
        is Screen.Practice -> "অনুশীলন"
        is Screen.ModelTest -> "মডেল টেস্ট"
        is Screen.Progress -> "আমার অগ্রগতি"
        is Screen.Profile -> "শিক্ষার্থী প্রোফাইল"
        is Screen.Bookmarks -> "সংরক্ষিত বুকমার্ক"
        is Screen.Downloads -> "অফলাইন ডাউনলোড"
        is Screen.Search -> "অনুসন্ধান"
        is Screen.AdminCms -> "কন্টেন্ট ম্যানেজমেন্ট (CMS)"
        is Screen.StudyGroups -> "স্টাডি গ্রুপ ও শিক্ষা চ্যাট"
        is Screen.StudyGroupChat -> viewModel.getStudyGroup(currentScreen.groupId)?.nameBn ?: "গ্রুপ চ্যাট"
        is Screen.GroupModeration -> "মডারেশন ও নিরাপত্তা কেন্দ্র"
        is Screen.ChapterList -> viewModel.getSubject(currentScreen.subjectId)?.titleBn ?: "অধ্যায়সমূহ"
        is Screen.LessonDetail -> "পাঠের বিবরণ"
        is Screen.McqQuiz -> "কুইজ পরীক্ষা"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LearnEasyTopBar(
                title = screenTitle,
                subtitle = if (isTopLevelScreen) "সহজে শিখি, আত্মবিশ্বাসে এগিয়ে যাই" else null,
                currentClass = currentClass,
                isDarkMode = isDarkMode,
                unreadNotificationsCount = unreadNotifsCount,
                onClassClick = { showClassDialog = true },
                onSearchClick = { navigateTo(Screen.Search) },
                onBookmarkClick = { navigateTo(Screen.Bookmarks) },
                onNotificationClick = { showNotificationsDialog = true },
                onDarkModeToggle = { viewModel.toggleDarkMode() },
                showBackButton = !isTopLevelScreen,
                onBackClick = { navigateBack() }
            )
        },
        bottomBar = {
            if (isTopLevelScreen) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    NavigationBarItem(
                        selected = currentScreen is Screen.Home,
                        onClick = { switchBottomTab(Screen.Home) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "হোম") },
                        label = { Text("হোম", style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.testTag("nav_home"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                    NavigationBarItem(
                        selected = currentScreen is Screen.Courses,
                        onClick = { switchBottomTab(Screen.Courses) },
                        icon = { Icon(Icons.Default.School, contentDescription = "কোর্স") },
                        label = { Text("কোর্স", style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.testTag("nav_courses"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                    NavigationBarItem(
                        selected = currentScreen is Screen.Practice,
                        onClick = { switchBottomTab(Screen.Practice) },
                        icon = { Icon(Icons.Default.Psychology, contentDescription = "অনুশীলন") },
                        label = { Text("অনুশীলন", style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.testTag("nav_practice"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                    NavigationBarItem(
                        selected = currentScreen is Screen.Progress,
                        onClick = { switchBottomTab(Screen.Progress) },
                        icon = { Icon(Icons.Default.TrendingUp, contentDescription = "অগ্রগতি") },
                        label = { Text("অগ্রগতি", style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.testTag("nav_progress"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                    NavigationBarItem(
                        selected = currentScreen is Screen.Profile,
                        onClick = { switchBottomTab(Screen.Profile) },
                        icon = { Icon(Icons.Default.Person, contentDescription = "প্রোফাইল") },
                        label = { Text("প্রোফাইল", style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.testTag("nav_profile"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { targetScreen ->
                when (targetScreen) {
                    is Screen.Home -> {
                        HomeScreen(
                            viewModel = viewModel,
                            onNavigateToSubjects = { navigateTo(Screen.Subjects) },
                            onNavigateToSubjectDetail = { subjectId ->
                                navigateTo(Screen.ChapterList(subjectId))
                            },
                            onNavigateToPractice = { switchBottomTab(Screen.Practice) },
                            onNavigateToDailyQuiz = {
                                viewModel.startDailyQuiz()
                                navigateTo(Screen.McqQuiz)
                            },
                            onNavigateToModelTest = { navigateTo(Screen.ModelTest) },
                            onNavigateToProgress = { switchBottomTab(Screen.Progress) },
                            onNavigateToBookmarks = { navigateTo(Screen.Bookmarks) },
                            onNavigateToCourses = { switchBottomTab(Screen.Courses) },
                            onNavigateToDownloads = { navigateTo(Screen.Downloads) },
                            onNavigateToLesson = { lessonId ->
                                navigateTo(Screen.LessonDetail(lessonId))
                            },
                            onNavigateToStudyGroups = { navigateTo(Screen.StudyGroups) },
                            onNavigateToStudyGroupChat = { groupId ->
                                navigateTo(Screen.StudyGroupChat(groupId))
                            }
                        )
                    }

                    is Screen.Courses -> {
                        CoursesScreen(
                            viewModel = viewModel,
                            onNavigateToLesson = { lessonId ->
                                navigateTo(Screen.LessonDetail(lessonId))
                            }
                        )
                    }

                    is Screen.Subjects -> {
                        SubjectsScreen(
                            viewModel = viewModel,
                            onSubjectClick = { subjectId ->
                                navigateTo(Screen.ChapterList(subjectId))
                            }
                        )
                    }

                    is Screen.ChapterList -> {
                        ChapterListScreen(
                            subjectId = targetScreen.subjectId,
                            viewModel = viewModel,
                            onLessonClick = { lessonId ->
                                navigateTo(Screen.LessonDetail(lessonId))
                            },
                            onStartChapterQuiz = { chapterId, chapterTitle, subjectId ->
                                viewModel.startChapterQuiz(chapterId, chapterTitle, subjectId)
                                navigateTo(Screen.McqQuiz)
                            }
                        )
                    }

                    is Screen.LessonDetail -> {
                        LessonDetailScreen(
                            lessonId = targetScreen.lessonId,
                            viewModel = viewModel,
                            onBackClick = { navigateBack() },
                            onStartLessonQuiz = { lessonId, title, subjectId ->
                                viewModel.startLessonQuiz(lessonId, title, subjectId)
                                navigateTo(Screen.McqQuiz)
                            }
                        )
                    }

                    is Screen.Practice -> {
                        PracticeScreen(
                            viewModel = viewModel,
                            onStartPracticeQuiz = { subjectId ->
                                viewModel.startPracticeQuiz(subjectId)
                                navigateTo(Screen.McqQuiz)
                            }
                        )
                    }

                    is Screen.ModelTest -> {
                        ModelTestScreen(
                            viewModel = viewModel,
                            onStartTest = { modelTest ->
                                viewModel.startModelTest(modelTest)
                                navigateTo(Screen.McqQuiz)
                            }
                        )
                    }

                    is Screen.McqQuiz -> {
                        McqQuizScreen(
                            viewModel = viewModel,
                            onBackClick = { navigateBack() }
                        )
                    }

                    is Screen.Progress -> {
                        ProgressScreen(viewModel = viewModel)
                    }

                    is Screen.Bookmarks -> {
                        BookmarkScreen(
                            viewModel = viewModel,
                            onNavigateToLesson = { lessonId ->
                                navigateTo(Screen.LessonDetail(lessonId))
                            }
                        )
                    }

                    is Screen.Downloads -> {
                        DownloadsScreen(
                            viewModel = viewModel,
                            onNavigateToLesson = { lessonId ->
                                navigateTo(Screen.LessonDetail(lessonId))
                            }
                        )
                    }

                    is Screen.Search -> {
                        SearchScreen(
                            viewModel = viewModel,
                            onNavigateToLesson = { lessonId ->
                                navigateTo(Screen.LessonDetail(lessonId))
                            }
                        )
                    }

                    is Screen.Profile -> {
                        ProfileScreen(
                            viewModel = viewModel,
                            onNavigateToBookmarks = { navigateTo(Screen.Bookmarks) },
                            onNavigateToProgress = { switchBottomTab(Screen.Progress) },
                            onNavigateToDownloads = { navigateTo(Screen.Downloads) },
                            onNavigateToAdminCms = { navigateTo(Screen.AdminCms) },
                            onNavigateToStudyGroups = { navigateTo(Screen.StudyGroups) },
                            onNavigateToModeration = { navigateTo(Screen.GroupModeration("all")) }
                        )
                    }

                    is Screen.StudyGroups -> {
                        StudyGroupsScreen(
                            viewModel = viewModel,
                            onNavigateToChat = { groupId ->
                                navigateTo(Screen.StudyGroupChat(groupId))
                            },
                            onNavigateToModeration = { groupId ->
                                navigateTo(Screen.GroupModeration(groupId))
                            }
                        )
                    }

                    is Screen.StudyGroupChat -> {
                        StudyGroupChatScreen(
                            groupId = targetScreen.groupId,
                            viewModel = viewModel,
                            onBackClick = { navigateBack() },
                            onNavigateToModeration = { gid ->
                                navigateTo(Screen.GroupModeration(gid))
                            }
                        )
                    }

                    is Screen.GroupModeration -> {
                        GroupModerationScreen(
                            groupId = targetScreen.groupId,
                            viewModel = viewModel,
                            onBackClick = { navigateBack() }
                        )
                    }

                    is Screen.AdminCms -> {
                        AdminCmsScreen(
                            viewModel = viewModel,
                            onBackClick = { navigateBack() }
                        )
                    }
                }
            }
        }
    }
}
