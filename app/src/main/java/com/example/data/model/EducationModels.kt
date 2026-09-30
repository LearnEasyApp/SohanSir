package com.example.data.model

// Dynamic Academic Class System supporting Class 9, 10, SSC Prep and future expansion
data class AcademicClass(
    val id: String,
    val code: String,
    val titleBn: String,
    val shortName: String,
    val descriptionBn: String,
    val isEnabled: Boolean = true
)

// Legacy enum for backward compatibility with existing usages
enum class ClassLevel(val id: String, val titleBn: String, val shortName: String) {
    CLASS_9("CLASS_9", "নবম শ্রেণি", "৯ম"),
    CLASS_10("CLASS_10", "দশম শ্রেণি", "১০ম"),
    CLASS_SSC("CLASS_SSC", "এসএসসি পূর্ণাঙ্গ প্রস্তুতি", "এসএসসি");

    companion object {
        fun fromId(id: String): ClassLevel {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: CLASS_9
        }
    }
}

val PredefinedClasses = listOf(
    AcademicClass("CLASS_9", "9", "নবম শ্রেণি", "৯ম", "নবম শ্রেণির পূর্ণাঙ্গ সিলেবাস ও বেসিক প্রস্তুতি"),
    AcademicClass("CLASS_10", "10", "দশম শ্রেণি", "১০ম", "দশম শ্রেণির পূর্ণাঙ্গ পাঠ্যক্রম ও অধ্যায়ভিত্তিক অনুশীলন"),
    AcademicClass("CLASS_SSC", "SSC", "এসএসসি পূর্ণাঙ্গ প্রস্তুতি", "এসএসসি", "এসএসসি বোর্ড পরীক্ষার চূড়ান্ত মডেল টেস্ট ও রিভিশন")
)

enum class UserRole {
    STUDENT,
    TEACHER,
    ADMIN
}

enum class VideoProvider {
    YOUTUBE,
    DIRECT_URL,
    VIMEO
}

data class VideoLesson(
    val videoId: String,
    val provider: VideoProvider = VideoProvider.YOUTUBE,
    val videoTitleBn: String,
    val instructorName: String,
    val instructorTitle: String,
    val durationMinutes: Int = 12,
    val videoUrl: String = "https://www.youtube.com/watch?v=$videoId",
    val thumbnailUrl: String = ""
)

enum class ResourceType(val titleBn: String, val iconName: String) {
    PDF("পিডিএফ নোট", "picture_as_pdf"),
    LECTURE_NOTE("লেকচার নোট", "notes"),
    FORMULA_SHEET("সূত্র তালিকা", "functions"),
    WORKSHEET("ওয়ার্কশিট", "assignment"),
    QUESTION_PAPER("বোর্ড প্রশ্নপত্র", "quiz"),
    EXTERNAL_LINK("অনলাইন লিংক", "link")
}

data class LessonResource(
    val id: String,
    val lessonId: String,
    val titleBn: String,
    val resourceType: ResourceType,
    val fileUrl: String,
    val fileSizeText: String = "১.২ MB",
    val descriptionBn: String = "",
    val isDownloaded: Boolean = false
)

data class FormulaItem(
    val id: String,
    val nameBn: String,
    val formulaText: String,
    val explanationBn: String
)

data class ExampleItem(
    val id: String,
    val problemBn: String,
    val stepByStepSolutionBn: String,
    val tipBn: String = ""
)

data class ExerciseItem(
    val id: String,
    val questionBn: String,
    val hintBn: String,
    val answerBn: String
)

data class Lesson(
    val id: String,
    val chapterId: String,
    val subjectId: String,
    val lessonNumberBn: String,
    val titleBn: String,
    val summaryBn: String,
    val readTimeMinutes: Int = 5,
    val explanationBn: String,
    val definitionBn: String,
    val keyPoints: List<String> = emptyList(),
    val formulas: List<FormulaItem> = emptyList(),
    val examples: List<ExampleItem> = emptyList(),
    val commonMistakes: List<String> = emptyList(),
    val exercises: List<ExerciseItem> = emptyList(),
    val videoLesson: VideoLesson? = null,
    val resources: List<LessonResource> = emptyList(),
    val instructorName: String? = null
)

data class Chapter(
    val id: String,
    val subjectId: String,
    val chapterNumberBn: String,
    val titleBn: String,
    val descriptionBn: String,
    val lessons: List<Lesson> = emptyList(),
    val mcqCount: Int = 10,
    val resources: List<LessonResource> = emptyList()
)

data class Subject(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val descriptionBn: String,
    val colorHex: Long,
    val chapters: List<Chapter> = emptyList()
)

enum class ContentPublishStatus {
    DRAFT,
    PUBLISHED,
    ARCHIVED
}

data class Course(
    val id: String,
    val titleBn: String,
    val descriptionBn: String,
    val classId: String,
    val subjectId: String,
    val chapterId: String? = null,
    val instructorName: String,
    val instructorRole: String,
    val totalLessons: Int = 8,
    val estimatedDurationHours: String = "৬ ঘণ্টা",
    val difficultyBn: String = "সহজ থেকে মাঝারি",
    val status: ContentPublishStatus = ContentPublishStatus.PUBLISHED,
    val rating: Double = 4.9,
    val enrolledStudentsCount: Int = 1250,
    val lessonIds: List<String> = emptyList()
)

data class MCQQuestion(
    val id: String,
    val subjectId: String,
    val chapterId: String,
    val questionBn: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanationBn: String,
    val difficulty: String = "মাঝারি", // সহজ, মাঝারি, কঠিন
    val topicName: String = ""
)

data class QuizConfig(
    val hasNegativeMarking: Boolean = false,
    val marksPerQuestion: Double = 1.0,
    val negativeMarkPerWrong: Double = 0.25,
    val isRandomized: Boolean = true,
    val timeLimitMinutes: Int = 15
)

data class ModelTest(
    val id: String,
    val titleBn: String,
    val subjectId: String? = null,
    val classId: String = "CLASS_9",
    val durationMinutes: Int = 15,
    val totalMarks: Int = 15,
    val instructionsBn: String = "প্রতিটি প্রশ্নের মান ১। সময় শেষ হওয়ার পূর্বেই পরীক্ষা জমা দিন।",
    val config: QuizConfig = QuizConfig(),
    val questions: List<MCQQuestion>
)

data class StudyStreak(
    val currentStreakDays: Int = 3,
    val totalLessonsCompleted: Int = 0,
    val totalQuizzesTaken: Int = 0,
    val averageScorePercent: Int = 0,
    val totalLearningMinutes: Int = 180
)

data class AppNotification(
    val id: String,
    val titleBn: String,
    val messageBn: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "GENERAL", // DAILY_QUIZ, NEW_COURSE, MODEL_TEST, ANNOUNCEMENT
    val isRead: Boolean = false,
    val targetRoute: String = ""
)

data class WeakTopicSummary(
    val subjectId: String,
    val subjectTitleBn: String,
    val topicName: String,
    val wrongAttemptsCount: Int,
    val recommendationBn: String
)
