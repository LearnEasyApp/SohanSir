package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.BookmarkEntity
import com.example.data.db.CompletedLessonEntity
import com.example.data.db.CourseEnrollmentEntity
import com.example.data.db.DownloadedResourceEntity
import com.example.data.db.QuizResultEntity
import com.example.data.db.UserProfileEntity
import com.example.data.model.AcademicClass
import com.example.data.model.AppNotification
import com.example.data.model.ChatMessage
import com.example.data.model.ClassLevel
import com.example.data.model.Course
import com.example.data.model.FormulaItem
import com.example.data.model.Lesson
import com.example.data.model.LessonResource
import com.example.data.model.MCQQuestion
import com.example.data.model.MessageAttachment
import com.example.data.model.MessageReport
import com.example.data.model.ModelTest
import com.example.data.model.ModerationLog
import com.example.data.model.PredefinedClasses
import com.example.data.model.QuizConfig
import com.example.data.model.StudyGroup
import com.example.data.model.Subject
import com.example.data.model.UserReport
import com.example.data.model.UserRole
import com.example.data.model.WeakTopicSummary
import com.example.data.repository.ChatRepository
import com.example.data.repository.EducationRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ActiveQuizState(
    val title: String = "",
    val testType: String = "QUIZ", // "DAILY_QUIZ", "CHAPTER_PRACTICE", "MODEL_TEST", "LESSON_QUIZ"
    val subjectId: String = "",
    val questions: List<MCQQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswers: Map<Int, Int> = emptyMap(), // questionIndex -> optionIndex
    val isSubmitted: Boolean = false,
    val timeRemainingSeconds: Int = 0,
    val totalTimeSeconds: Int = 0,
    val isTimerActive: Boolean = false,
    val config: QuizConfig = QuizConfig()
) {
    val totalQuestions: Int get() = questions.size
    val correctCount: Int get() {
        var count = 0
        questions.forEachIndexed { index, q ->
            if (selectedAnswers[index] == q.correctOptionIndex) count++
        }
        return count
    }
    val wrongCount: Int get() {
        var count = 0
        questions.forEachIndexed { index, q ->
            val ans = selectedAnswers[index]
            if (ans != null && ans != q.correctOptionIndex) count++
        }
        return count
    }
    val skippedCount: Int get() = totalQuestions - (correctCount + wrongCount)
    val scorePercentage: Int get() = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0

    val netMarksObtained: Double get() {
        val positive = correctCount * config.marksPerQuestion
        val negative = if (config.hasNegativeMarking) wrongCount * config.negativeMarkPerWrong else 0.0
        val net = positive - negative
        return if (net > 0.0) net else 0.0
    }

    val wrongQuestions: List<MCQQuestion> get() {
        val list = mutableListOf<MCQQuestion>()
        questions.forEachIndexed { index, q ->
            val ans = selectedAnswers[index]
            if (ans != null && ans != q.correctOptionIndex) {
                list.add(q)
            }
        }
        return list
    }
}

data class SearchResultItem(
    val id: String,
    val type: String, // "LESSON", "FORMULA", "QUESTION", "COURSE", "RESOURCE"
    val title: String,
    val subtitle: String,
    val subjectId: String,
    val lessonId: String? = null,
    val courseId: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EducationRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = EducationRepository(db)
        initDefaultUser()
    }

    private fun initDefaultUser() {
        viewModelScope.launch {
            repository.saveUserProfile(UserProfileEntity())
        }
    }

    // Data streams
    val userProfile: StateFlow<UserProfileEntity?> = repository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfileEntity())

    val completedLessons: StateFlow<List<CompletedLessonEntity>> = repository.getAllCompletedLessons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.getAllBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizResults: StateFlow<List<QuizResultEntity>> = repository.getAllQuizResults()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val courseEnrollments: StateFlow<List<CourseEnrollmentEntity>> = repository.getCourseEnrollments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val downloadedResources: StateFlow<List<DownloadedResourceEntity>> = repository.getAllDownloads()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // In-memory Course & Notification state
    private val _courses = MutableStateFlow<List<Course>>(repository.getAllCourses())
    val courses: StateFlow<List<Course>> = _courses.asStateFlow()

    private val _notifications = MutableStateFlow<List<AppNotification>>(repository.getNotifications())
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    val unreadNotificationCount: StateFlow<Int> = _notifications.map { list ->
        list.count { !it.isRead }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    // User Role (STUDENT / TEACHER / ADMIN)
    private val _currentRole = MutableStateFlow(UserRole.STUDENT)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // Study Groups & Educational Chat
    val chatRepository = ChatRepository()
    val studyGroups: StateFlow<List<StudyGroup>> = chatRepository.studyGroups
    val messagesByGroup: StateFlow<Map<String, List<ChatMessage>>> = chatRepository.messagesByGroup
    val messageReports: StateFlow<List<MessageReport>> = chatRepository.messageReports
    val userReports: StateFlow<List<UserReport>> = chatRepository.userReports
    val moderationLogs: StateFlow<List<ModerationLog>> = chatRepository.moderationLogs
    val blockedUsers: StateFlow<Set<String>> = chatRepository.blockedUsers

    // Active Quiz / Test state
    private val _quizState = MutableStateFlow(ActiveQuizState())
    val quizState: StateFlow<ActiveQuizState> = _quizState.asStateFlow()

    private var timerJob: Job? = null

    // Search state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSearchCategory = MutableStateFlow("ALL")
    val selectedSearchCategory: StateFlow<String> = _selectedSearchCategory.asStateFlow()

    // Academic Classes
    fun getAcademicClasses(): List<AcademicClass> = repository.getAcademicClasses()

    fun getSubjects(): List<Subject> = repository.getSubjects()

    fun getSubject(subjectId: String): Subject? = repository.getSubjectById(subjectId)

    fun getLesson(lessonId: String): Lesson? = repository.getLessonById(lessonId)

    fun getModelTests(): List<ModelTest> = repository.getModelTests()

    fun getAllQuestions(): List<MCQQuestion> = repository.getAllQuestions()

    fun getQuestionsForSubject(subjectId: String): List<MCQQuestion> = repository.getQuestionsForSubject(subjectId)

    // Courses
    fun getCourses(): List<Course> = _courses.value

    fun getCourse(courseId: String): Course? = repository.getCourseById(courseId)

    fun enrollInCourse(courseId: String) {
        viewModelScope.launch {
            repository.enrollInCourse(courseId)
        }
    }

    fun getCourseProgressPercent(course: Course): Int {
        val completed = completedLessons.value
        if (course.lessonIds.isEmpty()) return 40
        val doneCount = course.lessonIds.count { id -> completed.any { it.lessonId == id } }
        return (doneCount * 100) / course.lessonIds.size
    }

    // Downloads
    fun downloadResource(resource: LessonResource) {
        viewModelScope.launch {
            repository.saveDownloadedResource(resource)
        }
    }

    fun removeDownloadedResource(resourceId: String) {
        viewModelScope.launch {
            repository.removeDownloadedResource(resourceId)
        }
    }

    fun isResourceDownloaded(resourceId: String): Boolean {
        return downloadedResources.value.any { it.resourceId == resourceId }
    }

    // Notifications
    fun markNotificationAsRead(id: String) {
        repository.markNotificationAsRead(id)
        _notifications.value = repository.getNotifications()
    }

    fun markAllNotificationsAsRead() {
        val updated = _notifications.value.map { it.copy(isRead = true) }
        _notifications.value = updated
    }

    // Class selection
    fun switchClass(classLevel: ClassLevel) {
        viewModelScope.launch {
            repository.updateSelectedClass(classLevel.id)
        }
    }

    fun switchAcademicClass(academicClass: AcademicClass) {
        viewModelScope.launch {
            repository.updateSelectedClass(academicClass.id)
        }
    }

    fun toggleDarkMode() {
        val current = userProfile.value?.isDarkMode ?: false
        viewModelScope.launch {
            repository.updateDarkMode(!current)
        }
    }

    fun switchUserRole(role: UserRole) {
        _currentRole.value = role
        viewModelScope.launch {
            repository.updateUserRole(role.name)
        }
    }

    fun updateProfile(name: String, school: String, selectedClass: String) {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfileEntity()
            repository.saveUserProfile(
                current.copy(
                    studentName = name,
                    schoolName = school,
                    selectedClass = selectedClass
                )
            )
        }
    }

    fun onLessonOpened(lesson: Lesson) {
        viewModelScope.launch {
            repository.updateLastLesson(lesson.id, lesson.subjectId, lesson.titleBn)
            repository.addLearningMinutes(5)
        }
    }

    fun toggleLessonCompleted(lesson: Lesson) {
        viewModelScope.launch {
            repository.toggleLessonCompleted(lesson.id, lesson.subjectId, lesson.chapterId)
        }
    }

    fun toggleBookmark(
        id: String,
        type: String,
        title: String,
        subtitle: String,
        targetId: String,
        subjectId: String,
        extraContent: String = ""
    ) {
        viewModelScope.launch {
            repository.toggleBookmark(id, type, title, subtitle, targetId, subjectId, extraContent)
        }
    }

    fun removeBookmark(id: String) {
        viewModelScope.launch {
            repository.removeBookmark(id)
        }
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.resetAllProgress()
        }
    }

    // Weak Topic Analysis
    fun getWeakTopics(): List<WeakTopicSummary> {
        return repository.getWeakTopicSummaries(quizResults.value)
    }

    // ==========================================
    // Quiz / Model Test Engine
    // ==========================================
    fun startDailyQuiz() {
        val questions = repository.getAllQuestions().shuffled().take(5)
        startQuizSession(
            title = "আজকের দৈনিক কুইজ",
            testType = "DAILY_QUIZ",
            subjectId = "ALL",
            questions = questions,
            durationMinutes = 5,
            config = QuizConfig(hasNegativeMarking = false, marksPerQuestion = 1.0)
        )
    }

    fun startLessonQuiz(lessonId: String, lessonTitle: String, subjectId: String) {
        val questions = repository.getAllQuestions().filter { it.subjectId == subjectId }.take(5)
        startQuizSession(
            title = "$lessonTitle - কুইজ",
            testType = "LESSON_QUIZ",
            subjectId = subjectId,
            questions = questions,
            durationMinutes = 5
        )
    }

    fun startChapterQuiz(chapterId: String, chapterTitle: String, subjectId: String) {
        val questions = repository.getQuestionsForChapter(chapterId)
        val finalQuestions = if (questions.isNotEmpty()) questions else repository.getQuestionsForSubject(subjectId).take(5)
        startQuizSession(
            title = "$chapterTitle - কুইজ",
            testType = "CHAPTER_PRACTICE",
            subjectId = subjectId,
            questions = finalQuestions,
            durationMinutes = 6
        )
    }

    fun startModelTest(modelTest: ModelTest) {
        val questions = if (modelTest.config.isRandomized) modelTest.questions.shuffled() else modelTest.questions
        startQuizSession(
            title = modelTest.titleBn,
            testType = "MODEL_TEST",
            subjectId = modelTest.subjectId ?: "ALL",
            questions = questions,
            durationMinutes = modelTest.durationMinutes,
            config = modelTest.config
        )
    }

    fun startPracticeQuiz(subjectId: String) {
        val questions = repository.getQuestionsForSubject(subjectId)
        val subject = repository.getSubjectById(subjectId)
        val title = "${subject?.titleBn ?: "বিষয়ভিত্তিক"} অনুশীলন"
        startQuizSession(
            title = title,
            testType = "CHAPTER_PRACTICE",
            subjectId = subjectId,
            questions = questions,
            durationMinutes = 10
        )
    }

    fun startWeakTopicQuiz(weakTopic: WeakTopicSummary) {
        val questions = repository.getQuestionsForSubject(weakTopic.subjectId)
        startQuizSession(
            title = "${weakTopic.subjectTitleBn} দুর্বল টপিক রিভিশন",
            testType = "PRACTICE",
            subjectId = weakTopic.subjectId,
            questions = questions,
            durationMinutes = 8
        )
    }

    private fun startQuizSession(
        title: String,
        testType: String,
        subjectId: String,
        questions: List<MCQQuestion>,
        durationMinutes: Int,
        config: QuizConfig = QuizConfig()
    ) {
        timerJob?.cancel()
        val totalSecs = durationMinutes * 60
        _quizState.value = ActiveQuizState(
            title = title,
            testType = testType,
            subjectId = subjectId,
            questions = questions,
            currentQuestionIndex = 0,
            selectedAnswers = emptyMap(),
            isSubmitted = false,
            timeRemainingSeconds = totalSecs,
            totalTimeSeconds = totalSecs,
            isTimerActive = true,
            config = config
        )

        // Start countdown timer
        timerJob = viewModelScope.launch {
            while (_quizState.value.timeRemainingSeconds > 0 && !_quizState.value.isSubmitted) {
                delay(1000)
                val currentSecs = _quizState.value.timeRemainingSeconds - 1
                _quizState.value = _quizState.value.copy(timeRemainingSeconds = currentSecs)
                if (currentSecs <= 0) {
                    submitQuiz()
                    break
                }
            }
        }
    }

    fun selectAnswer(questionIndex: Int, optionIndex: Int) {
        if (_quizState.value.isSubmitted) return
        val updated = _quizState.value.selectedAnswers.toMutableMap()
        updated[questionIndex] = optionIndex
        _quizState.value = _quizState.value.copy(selectedAnswers = updated)
    }

    fun goToQuestion(index: Int) {
        if (index in 0 until _quizState.value.totalQuestions) {
            _quizState.value = _quizState.value.copy(currentQuestionIndex = index)
        }
    }

    fun submitQuiz() {
        if (_quizState.value.isSubmitted) return
        timerJob?.cancel()
        val state = _quizState.value
        val finalState = state.copy(isSubmitted = true, isTimerActive = false)
        _quizState.value = finalState

        // Save result to Room
        viewModelScope.launch {
            val timeTaken = finalState.totalTimeSeconds - finalState.timeRemainingSeconds
            val wrongIds = finalState.wrongQuestions.map { it.id }
            repository.saveQuizResult(
                testType = finalState.testType,
                subjectId = finalState.subjectId,
                title = finalState.title,
                totalQuestions = finalState.totalQuestions,
                correctAnswers = finalState.correctCount,
                wrongAnswers = finalState.wrongCount,
                skippedAnswers = finalState.skippedCount,
                timeTakenSeconds = if (timeTaken > 0) timeTaken else 1,
                wrongQuestionIds = wrongIds
            )
        }
    }

    fun retryQuiz() {
        val state = _quizState.value
        startQuizSession(
            title = state.title,
            testType = state.testType,
            subjectId = state.subjectId,
            questions = state.questions.shuffled(),
            durationMinutes = state.totalTimeSeconds / 60,
            config = state.config
        )
    }

    // ==========================================
    // Search Engine
    // ==========================================
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onSearchCategoryChanged(cat: String) {
        _selectedSearchCategory.value = cat
    }

    fun getSearchResults(): List<SearchResultItem> {
        val query = _searchQuery.value.trim().lowercase()
        if (query.isEmpty()) return emptyList()

        val category = _selectedSearchCategory.value
        val results = mutableListOf<SearchResultItem>()

        // Search Courses
        for (course in _courses.value) {
            if (category != "ALL" && course.subjectId != category) continue
            if (course.titleBn.lowercase().contains(query) || course.descriptionBn.lowercase().contains(query)) {
                results.add(
                    SearchResultItem(
                        id = course.id,
                        type = "COURSE",
                        title = course.titleBn,
                        subtitle = "কোর্স • ${course.instructorName} (${course.difficultyBn})",
                        subjectId = course.subjectId,
                        courseId = course.id
                    )
                )
            }
        }

        // Search Lessons
        for (subject in repository.getSubjects()) {
            if (category != "ALL" && subject.id != category) continue
            for (chapter in subject.chapters) {
                for (lesson in chapter.lessons) {
                    val matchTitle = lesson.titleBn.lowercase().contains(query)
                    val matchSummary = lesson.summaryBn.lowercase().contains(query)
                    val matchExplanation = lesson.explanationBn.lowercase().contains(query)
                    if (matchTitle || matchSummary || matchExplanation) {
                        results.add(
                            SearchResultItem(
                                id = lesson.id,
                                type = "LESSON",
                                title = lesson.titleBn,
                                subtitle = "${subject.titleBn} • ${chapter.titleBn}",
                                subjectId = subject.id,
                                lessonId = lesson.id
                            )
                        )
                    }

                    // Search formulas
                    for (formula in lesson.formulas) {
                        if (formula.nameBn.lowercase().contains(query) || formula.formulaText.lowercase().contains(query)) {
                            results.add(
                                SearchResultItem(
                                    id = formula.id,
                                    type = "FORMULA",
                                    title = formula.nameBn,
                                    subtitle = "${formula.formulaText} (${subject.titleBn})",
                                    subjectId = subject.id,
                                    lessonId = lesson.id
                                )
                            )
                        }
                    }

                    // Search resources
                    for (res in lesson.resources) {
                        if (res.titleBn.lowercase().contains(query) || res.descriptionBn.lowercase().contains(query)) {
                            results.add(
                                SearchResultItem(
                                    id = res.id,
                                    type = "RESOURCE",
                                    title = res.titleBn,
                                    subtitle = "${res.resourceType.titleBn} • ${subject.titleBn}",
                                    subjectId = subject.id,
                                    lessonId = lesson.id
                                )
                            )
                        }
                    }
                }
            }
        }

        // Search Questions
        for (q in repository.getAllQuestions()) {
            if (category != "ALL" && q.subjectId != category) continue
            if (q.questionBn.lowercase().contains(query) || q.explanationBn.lowercase().contains(query)) {
                results.add(
                    SearchResultItem(
                        id = q.id,
                        type = "QUESTION",
                        title = q.questionBn,
                        subtitle = "কুইজ প্রশ্ন (${repository.getSubjectById(q.subjectId)?.titleBn ?: ""})",
                        subjectId = q.subjectId
                    )
                )
            }
        }

        return results
    }

    // Stats calculations
    fun getTotalLessonsCount(): Int {
        var count = 0
        for (s in repository.getSubjects()) {
            for (ch in s.chapters) {
                count += ch.lessons.size
            }
        }
        return count
    }

    fun getSubjectProgress(subjectId: String): Pair<Int, Int> {
        val subject = repository.getSubjectById(subjectId) ?: return Pair(0, 0)
        var total = 0
        var completed = 0
        val completedList = completedLessons.value
        for (ch in subject.chapters) {
            for (les in ch.lessons) {
                total++
                if (completedList.any { it.lessonId == les.id }) {
                    completed++
                }
            }
        }
        return Pair(completed, total)
    }

    // Admin CMS Functions
    fun adminAddCourse(course: Course) {
        repository.adminAddCourse(course)
        _courses.value = repository.getAllCourses()
    }

    fun adminUpdateCourse(course: Course) {
        repository.adminUpdateCourse(course)
        _courses.value = repository.getAllCourses()
    }

    fun adminDeleteCourse(courseId: String) {
        repository.adminDeleteCourse(courseId)
        _courses.value = repository.getAllCourses()
    }

    fun adminToggleCourseStatus(courseId: String) {
        repository.adminToggleCourseStatus(courseId)
        _courses.value = repository.getAllCourses()
    }

    // ==========================================
    // Study Groups & Chat System Operations
    // ==========================================

    fun getStudyGroup(groupId: String): StudyGroup? = chatRepository.getGroupById(groupId)

    fun getMessagesForGroup(groupId: String): List<ChatMessage> = chatRepository.getMessagesForGroup(groupId)

    fun sendChatMessage(
        groupId: String,
        text: String,
        replyToId: String? = null,
        replyToName: String? = null,
        replyToText: String? = null,
        attachment: MessageAttachment? = null,
        isAnnouncement: Boolean = false
    ) {
        val user = userProfile.value
        val senderName = user?.studentName ?: "সাদিয়া রহমান"
        val role = currentRole.value
        val school = user?.schoolName ?: "গভর্নমেন্ট ল্যাবরেটরি হাই স্কুল"

        val msg = chatRepository.sendMessage(
            groupId = groupId,
            senderName = senderName,
            senderRole = role,
            senderSchool = school,
            text = text,
            replyToMessageId = replyToId,
            replyToSenderName = replyToName,
            replyToText = replyToText,
            attachment = attachment,
            isAnnouncement = isAnnouncement
        )

        // If announcement or reply, trigger notification
        if (isAnnouncement) {
            val groupTitle = getStudyGroup(groupId)?.nameBn ?: "স্টাডি গ্রুপ"
            val newNotif = AppNotification(
                id = "notif_ann_" + System.currentTimeMillis(),
                titleBn = "নতুন ঘোষণা: $groupTitle",
                messageBn = text.take(60) + if (text.length > 60) "..." else "",
                timestamp = System.currentTimeMillis(),
                isRead = false,
                targetRoute = "study_group_$groupId"
            )
            _notifications.value = listOf(newNotif) + _notifications.value
        }
    }

    fun editChatMessage(messageId: String, groupId: String, newText: String) {
        chatRepository.editMessage(messageId, groupId, newText)
    }

    fun deleteChatMessage(messageId: String, groupId: String) {
        val user = userProfile.value
        val role = currentRole.value
        val userName = user?.studentName ?: "ব্যবহারকারী"
        chatRepository.deleteMessage(
            messageId = messageId,
            groupId = groupId,
            deletedByRole = role,
            moderatorName = if (role != UserRole.STUDENT) "$userName (${if (role == UserRole.TEACHER) "শিক্ষক" else "অ্যাডমিন"})" else null
        )
    }

    fun toggleMessageReaction(messageId: String, groupId: String, emoji: String) {
        val userName = userProfile.value?.studentName ?: "সাদিয়া রহমান"
        chatRepository.toggleReaction(messageId, groupId, emoji, userName)
    }

    fun togglePinChatMessage(messageId: String, groupId: String) {
        val role = currentRole.value
        if (role == UserRole.STUDENT) return
        val userName = userProfile.value?.studentName ?: "মডারেটর"
        val roleName = if (role == UserRole.TEACHER) "শিক্ষক" else "অ্যাডমিন"
        chatRepository.togglePinMessage(messageId, groupId, "$userName ($roleName)")
    }

    fun muteStudentInGroup(groupId: String, studentName: String, reasonBn: String) {
        val role = currentRole.value
        if (role == UserRole.STUDENT) return
        val userName = userProfile.value?.studentName ?: "মডারেটর"
        val roleName = if (role == UserRole.TEACHER) "শিক্ষক" else "অ্যাডমিন"
        chatRepository.muteStudent(groupId, studentName, "$userName ($roleName)", reasonBn)
    }

    fun unmuteStudentInGroup(groupId: String, studentName: String) {
        val role = currentRole.value
        if (role == UserRole.STUDENT) return
        val userName = userProfile.value?.studentName ?: "মডারেটর"
        val roleName = if (role == UserRole.TEACHER) "শিক্ষক" else "অ্যাডমিন"
        chatRepository.unmuteStudent(groupId, studentName, "$userName ($roleName)")
    }

    fun removeStudentFromGroup(groupId: String, studentName: String) {
        val role = currentRole.value
        if (role == UserRole.STUDENT) return
        val userName = userProfile.value?.studentName ?: "মডারেটর"
        val roleName = if (role == UserRole.TEACHER) "শিক্ষক" else "অ্যাডমিন"
        chatRepository.removeStudentFromGroup(groupId, studentName, "$userName ($roleName)")
    }

    fun addStudentToGroup(groupId: String, studentName: String) {
        val role = currentRole.value
        if (role == UserRole.STUDENT) return
        val userName = userProfile.value?.studentName ?: "মডারেটর"
        val roleName = if (role == UserRole.TEACHER) "শিক্ষক" else "অ্যাডমিন"
        chatRepository.addStudentToGroup(groupId, studentName, "$userName ($roleName)")
    }

    fun createStudyGroup(group: StudyGroup) {
        val role = currentRole.value
        if (role == UserRole.STUDENT) return
        val userName = userProfile.value?.studentName ?: "অ্যাডমিন"
        val roleName = if (role == UserRole.TEACHER) "শিক্ষক" else "অ্যাডমিন"
        chatRepository.createStudyGroup(group, "$userName ($roleName)")
    }

    fun updateStudyGroup(group: StudyGroup) {
        val role = currentRole.value
        if (role == UserRole.STUDENT) return
        val userName = userProfile.value?.studentName ?: "মডারেটর"
        val roleName = if (role == UserRole.TEACHER) "শিক্ষক" else "অ্যাডমিন"
        chatRepository.updateStudyGroup(group, "$userName ($roleName)")
    }

    fun reportMessage(
        messageId: String,
        groupId: String,
        groupName: String,
        reportedText: String,
        reportedSender: String,
        reasonBn: String
    ) {
        val reporter = userProfile.value?.studentName ?: "সাদিয়া রহমান"
        chatRepository.reportMessage(
            messageId = messageId,
            groupId = groupId,
            groupName = groupName,
            reportedText = reportedText,
            reportedSender = reportedSender,
            reporterName = reporter,
            reasonBn = reasonBn
        )
    }

    fun reportUser(
        targetUserName: String,
        groupId: String,
        groupName: String,
        reasonBn: String
    ) {
        val reporter = userProfile.value?.studentName ?: "সাদিয়া রহমান"
        chatRepository.reportUser(
            targetUserName = targetUserName,
            reporterName = reporter,
            groupId = groupId,
            groupName = groupName,
            reasonBn = reasonBn
        )
    }

    fun toggleBlockUser(userName: String): Boolean {
        return chatRepository.toggleBlockUser(userName)
    }

    fun resolveMessageReport(reportId: String, actionTakenBn: String) {
        val role = currentRole.value
        if (role == UserRole.STUDENT) return
        val userName = userProfile.value?.studentName ?: "মডারেটর"
        val roleName = if (role == UserRole.TEACHER) "শিক্ষক" else "অ্যাডমিন"
        chatRepository.resolveReport(reportId, actionTakenBn, "$userName ($roleName)")
    }

    fun dismissMessageReport(reportId: String) {
        val role = currentRole.value
        if (role == UserRole.STUDENT) return
        val userName = userProfile.value?.studentName ?: "মডারেটর"
        val roleName = if (role == UserRole.TEACHER) "শিক্ষক" else "অ্যাডমিন"
        chatRepository.dismissReport(reportId, "$userName ($roleName)")
    }
}
