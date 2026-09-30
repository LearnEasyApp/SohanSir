package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completed_lessons")
data class CompletedLessonEntity(
    @PrimaryKey val lessonId: String,
    val subjectId: String,
    val chapterId: String,
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String, // e.g. "LESSON_xxx", "FORMULA_xxx", "QUESTION_xxx", "RESOURCE_xxx"
    val type: String, // "LESSON", "FORMULA", "QUESTION", "RESOURCE"
    val title: String,
    val subtitle: String,
    val targetId: String, // lessonId or chapterId or questionId or resourceId
    val subjectId: String,
    val extraContent: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val testType: String, // "DAILY_QUIZ", "PRACTICE", "MODEL_TEST", "LESSON_QUIZ"
    val subjectId: String,
    val title: String,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val skippedAnswers: Int = 0,
    val scorePercentage: Int,
    val timeTakenSeconds: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val wrongQuestionsIds: String = "" // comma-separated question IDs for weak-topic analysis
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val studentName: String = "সাদিয়া রহমান",
    val schoolName: String = "গভর্নমেন্ট ল্যাবরেটরি হাই স্কুল",
    val selectedClass: String = "CLASS_9",
    val isDarkMode: Boolean = false,
    val lastLessonId: String = "math_ch1_les1",
    val lastSubjectId: String = "MATH",
    val lastLessonTitle: String = "বাস্তব সংখ্যার শ্রেণিবিভাগ",
    val userRole: String = "STUDENT", // "STUDENT", "TEACHER", "ADMIN"
    val enrolledCourseIds: String = "course_math_ssc,course_physics_ssc",
    val totalLearningMinutes: Int = 240
)

@Entity(tableName = "course_enrollments")
data class CourseEnrollmentEntity(
    @PrimaryKey val courseId: String,
    val enrolledAt: Long = System.currentTimeMillis(),
    val progressPercent: Int = 0,
    val lastLessonId: String = ""
)

@Entity(tableName = "downloaded_resources")
data class DownloadedResourceEntity(
    @PrimaryKey val resourceId: String,
    val lessonId: String,
    val title: String,
    val resourceType: String,
    val fileSize: String,
    val downloadedAt: Long = System.currentTimeMillis(),
    val localUri: String = ""
)

@Entity(tableName = "app_notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val titleBn: String,
    val messageBn: String,
    val type: String = "GENERAL",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val targetRoute: String = ""
)
