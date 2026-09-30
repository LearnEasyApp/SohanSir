package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.db.BookmarkEntity
import com.example.data.db.CompletedLessonEntity
import com.example.data.db.CourseEnrollmentEntity
import com.example.data.db.DownloadedResourceEntity
import com.example.data.db.NotificationEntity
import com.example.data.db.QuizResultEntity
import com.example.data.db.UserProfileEntity
import com.example.data.model.AcademicClass
import com.example.data.model.AppNotification
import com.example.data.model.Chapter
import com.example.data.model.Course
import com.example.data.model.Lesson
import com.example.data.model.LessonResource
import com.example.data.model.MCQQuestion
import com.example.data.model.ModelTest
import com.example.data.model.PredefinedClasses
import com.example.data.model.Subject
import com.example.data.model.WeakTopicSummary
import com.example.data.source.EducationalContentDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class EducationRepository(private val database: AppDatabase) {

    private val completedLessonDao = database.completedLessonDao()
    private val bookmarkDao = database.bookmarkDao()
    private val quizResultDao = database.quizResultDao()
    private val userProfileDao = database.userProfileDao()
    private val courseEnrollmentDao = database.courseEnrollmentDao()
    private val downloadedResourceDao = database.downloadedResourceDao()
    private val notificationDao = database.notificationDao()

    // Classes
    fun getAcademicClasses(): List<AcademicClass> = PredefinedClasses

    // Educational Content
    fun getSubjects(): List<Subject> = EducationalContentDataSource.subjects

    fun getSubjectById(subjectId: String): Subject? = EducationalContentDataSource.getSubjectById(subjectId)

    fun getChapterById(chapterId: String): Chapter? = EducationalContentDataSource.getChapterById(chapterId)

    fun getLessonById(lessonId: String): Lesson? = EducationalContentDataSource.getLessonById(lessonId)

    fun getQuestionsForChapter(chapterId: String): List<MCQQuestion> =
        EducationalContentDataSource.getQuestionsForChapter(chapterId)

    fun getQuestionsForSubject(subjectId: String): List<MCQQuestion> =
        EducationalContentDataSource.getQuestionsForSubject(subjectId)

    fun getAllQuestions(): List<MCQQuestion> = EducationalContentDataSource.allQuestions

    fun getModelTests(): List<ModelTest> = EducationalContentDataSource.modelTests

    fun getModelTestById(testId: String): ModelTest? =
        EducationalContentDataSource.modelTests.find { it.id == testId }

    // Course System
    fun getAllCourses(): List<Course> = EducationalContentDataSource.getAllCourses()

    fun getCourseById(courseId: String): Course? = EducationalContentDataSource.getCourseById(courseId)

    fun getCoursesForSubject(subjectId: String): List<Course> =
        EducationalContentDataSource.getCoursesForSubject(subjectId)

    fun getCoursesForClass(classId: String): List<Course> =
        EducationalContentDataSource.getCoursesForClass(classId)

    fun getCourseEnrollments(): Flow<List<CourseEnrollmentEntity>> =
        courseEnrollmentDao.getAllEnrollments()

    fun getEnrollmentForCourse(courseId: String): Flow<CourseEnrollmentEntity?> =
        courseEnrollmentDao.getEnrollment(courseId)

    suspend fun enrollInCourse(courseId: String, initialLessonId: String = "") {
        courseEnrollmentDao.enroll(
            CourseEnrollmentEntity(
                courseId = courseId,
                lastLessonId = initialLessonId,
                progressPercent = 0
            )
        )
    }

    suspend fun updateCourseProgress(courseId: String, percent: Int, lessonId: String) {
        courseEnrollmentDao.updateProgress(courseId, percent, lessonId)
    }

    // Completed Lessons
    fun getAllCompletedLessons(): Flow<List<CompletedLessonEntity>> =
        completedLessonDao.getAllCompletedLessons()

    fun isLessonCompleted(lessonId: String): Flow<Boolean> =
        completedLessonDao.isLessonCompleted(lessonId)

    suspend fun toggleLessonCompleted(lessonId: String, subjectId: String, chapterId: String): Boolean {
        val isCurrentlyCompleted = completedLessonDao.isLessonCompleted(lessonId).firstOrNull() ?: false
        if (isCurrentlyCompleted) {
            completedLessonDao.unmarkCompleted(lessonId)
            return false
        } else {
            completedLessonDao.markCompleted(
                CompletedLessonEntity(
                    lessonId = lessonId,
                    subjectId = subjectId,
                    chapterId = chapterId
                )
            )
            return true
        }
    }

    // Bookmarks
    fun getAllBookmarks(): Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()

    fun getBookmarksByType(type: String): Flow<List<BookmarkEntity>> =
        bookmarkDao.getBookmarksByType(type)

    fun isBookmarked(id: String): Flow<Boolean> = bookmarkDao.isBookmarked(id)

    suspend fun toggleBookmark(
        id: String,
        type: String,
        title: String,
        subtitle: String,
        targetId: String,
        subjectId: String,
        extraContent: String = ""
    ): Boolean {
        val isSaved = bookmarkDao.isBookmarked(id).firstOrNull() ?: false
        if (isSaved) {
            bookmarkDao.deleteBookmarkById(id)
            return false
        } else {
            bookmarkDao.insertBookmark(
                BookmarkEntity(
                    id = id,
                    type = type,
                    title = title,
                    subtitle = subtitle,
                    targetId = targetId,
                    subjectId = subjectId,
                    extraContent = extraContent
                )
            )
            return true
        }
    }

    suspend fun removeBookmark(id: String) {
        bookmarkDao.deleteBookmarkById(id)
    }

    // Downloaded Resources
    fun getAllDownloads(): Flow<List<DownloadedResourceEntity>> =
        downloadedResourceDao.getAllDownloads()

    fun isResourceDownloaded(resourceId: String): Flow<Boolean> =
        downloadedResourceDao.isDownloaded(resourceId)

    suspend fun saveDownloadedResource(resource: LessonResource) {
        downloadedResourceDao.saveDownload(
            DownloadedResourceEntity(
                resourceId = resource.id,
                lessonId = resource.lessonId,
                title = resource.titleBn,
                resourceType = resource.resourceType.name,
                fileSize = resource.fileSizeText
            )
        )
    }

    suspend fun removeDownloadedResource(resourceId: String) {
        downloadedResourceDao.deleteDownload(resourceId)
    }

    // Notifications
    fun getNotifications(): List<AppNotification> = EducationalContentDataSource.getAllNotifications()

    fun markNotificationAsRead(id: String) {
        EducationalContentDataSource.markNotificationAsRead(id)
    }

    // Quiz & Test Results
    fun getAllQuizResults(): Flow<List<QuizResultEntity>> = quizResultDao.getAllResults()

    suspend fun saveQuizResult(
        testType: String,
        subjectId: String,
        title: String,
        totalQuestions: Int,
        correctAnswers: Int,
        wrongAnswers: Int,
        skippedAnswers: Int,
        timeTakenSeconds: Int,
        wrongQuestionIds: List<String> = emptyList()
    ): Long {
        val percentage = if (totalQuestions > 0) (correctAnswers * 100) / totalQuestions else 0
        return quizResultDao.insertResult(
            QuizResultEntity(
                testType = testType,
                subjectId = subjectId,
                title = title,
                totalQuestions = totalQuestions,
                correctAnswers = correctAnswers,
                wrongAnswers = wrongAnswers,
                skippedAnswers = skippedAnswers,
                scorePercentage = percentage,
                timeTakenSeconds = timeTakenSeconds,
                wrongQuestionsIds = wrongQuestionIds.joinToString(",")
            )
        )
    }

    // Weak Topic Analysis based on quiz errors
    fun getWeakTopicSummaries(quizResults: List<QuizResultEntity>): List<WeakTopicSummary> {
        val wrongIdSet = mutableSetOf<String>()
        quizResults.forEach { result ->
            if (result.wrongQuestionsIds.isNotEmpty()) {
                wrongIdSet.addAll(result.wrongQuestionsIds.split(",").filter { it.isNotBlank() })
            }
        }

        if (wrongIdSet.isEmpty()) {
            // Default recommendations if no errors yet
            return listOf(
                WeakTopicSummary(
                    subjectId = "MATH",
                    subjectTitleBn = "গণিত",
                    topicName = "বাস্তব সংখ্যা ও অমূলদ সংখ্যার প্রমাণ",
                    wrongAttemptsCount = 1,
                    recommendationBn = "√২ অমূলদ সংখ্যা প্রমাণ করার উপপাদ্যটি আরেকবার রিভিশন করুন।"
                ),
                WeakTopicSummary(
                    subjectId = "PHYSICS",
                    subjectTitleBn = "পদার্থবিজ্ঞান",
                    topicName = "গতির সমীকরণ ও একক রূপান্তর",
                    wrongAttemptsCount = 1,
                    recommendationBn = "v = u + at এবং s = ut + ½at² সূত্রের গাণিতিক উদাহরণগুলো অনুশীলন করুন।"
                )
            )
        }

        val allQ = EducationalContentDataSource.allQuestions
        val wrongQuestions = allQ.filter { wrongIdSet.contains(it.id) }
        val groupedBySubject = wrongQuestions.groupBy { it.subjectId }

        return groupedBySubject.map { (subjId, questions) ->
            val subjectTitle = EducationalContentDataSource.getSubjectById(subjId)?.titleBn ?: "সাধারণ বিষয়"
            WeakTopicSummary(
                subjectId = subjId,
                subjectTitleBn = subjectTitle,
                topicName = questions.firstOrNull()?.questionBn?.take(30) ?: "অধ্যায়ভিত্তিক রিভিশন",
                wrongAttemptsCount = questions.size,
                recommendationBn = "এই বিষয়ে ${questions.size}টি প্রশ্নে ভুল হয়েছে। ব্যাখ্যাসহ সমাধানগুলো পুনরায় অনুশীলন করুন।"
            )
        }
    }

    // User Profile
    fun getUserProfile(): Flow<UserProfileEntity?> = userProfileDao.getUserProfile()

    suspend fun saveUserProfile(profile: UserProfileEntity) {
        userProfileDao.saveUserProfile(profile)
    }

    suspend fun updateSelectedClass(classId: String) {
        userProfileDao.updateSelectedClass(classId)
    }

    suspend fun updateDarkMode(isDark: Boolean) {
        userProfileDao.updateDarkMode(isDark)
    }

    suspend fun updateLastLesson(lessonId: String, subjectId: String, title: String) {
        userProfileDao.updateLastLesson(lessonId, subjectId, title)
    }

    suspend fun updateUserRole(role: String) {
        userProfileDao.updateUserRole(role)
    }

    suspend fun addLearningMinutes(minutes: Int) {
        userProfileDao.addLearningMinutes(minutes)
    }

    suspend fun resetAllProgress() {
        completedLessonDao.clearAll()
        bookmarkDao.clearAll()
        quizResultDao.clearAll()
    }

    // Admin CMS Operations
    fun adminAddCourse(course: Course) {
        EducationalContentDataSource.adminAddCourse(course)
    }

    fun adminUpdateCourse(course: Course) {
        EducationalContentDataSource.adminUpdateCourse(course)
    }

    fun adminDeleteCourse(courseId: String) {
        EducationalContentDataSource.adminDeleteCourse(courseId)
    }

    fun adminToggleCourseStatus(courseId: String) {
        EducationalContentDataSource.adminToggleCourseStatus(courseId)
    }
}
