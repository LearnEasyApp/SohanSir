package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedLessonDao {
    @Query("SELECT * FROM completed_lessons")
    fun getAllCompletedLessons(): Flow<List<CompletedLessonEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM completed_lessons WHERE lessonId = :lessonId)")
    fun isLessonCompleted(lessonId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markCompleted(entity: CompletedLessonEntity)

    @Query("DELETE FROM completed_lessons WHERE lessonId = :lessonId")
    suspend fun unmarkCompleted(lessonId: String)

    @Query("DELETE FROM completed_lessons")
    suspend fun clearAll()
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks WHERE type = :type ORDER BY timestamp DESC")
    fun getBookmarksByType(type: String): Flow<List<BookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE id = :id)")
    fun isBookmarked(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmarkById(id: String)

    @Query("DELETE FROM bookmarks")
    suspend fun clearAll()
}

@Dao
interface QuizResultDao {
    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<QuizResultEntity>>

    @Query("SELECT * FROM quiz_results WHERE testType = :testType ORDER BY timestamp DESC")
    fun getResultsByType(testType: String): Flow<List<QuizResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: QuizResultEntity): Long

    @Query("DELETE FROM quiz_results")
    suspend fun clearAll()
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET selectedClass = :classId WHERE id = 1")
    suspend fun updateSelectedClass(classId: String)

    @Query("UPDATE user_profile SET isDarkMode = :isDark WHERE id = 1")
    suspend fun updateDarkMode(isDark: Boolean)

    @Query("UPDATE user_profile SET lastLessonId = :lessonId, lastSubjectId = :subjectId, lastLessonTitle = :title WHERE id = 1")
    suspend fun updateLastLesson(lessonId: String, subjectId: String, title: String)

    @Query("UPDATE user_profile SET userRole = :role WHERE id = 1")
    suspend fun updateUserRole(role: String)

    @Query("UPDATE user_profile SET enrolledCourseIds = :courseIds WHERE id = 1")
    suspend fun updateEnrolledCourses(courseIds: String)

    @Query("UPDATE user_profile SET totalLearningMinutes = totalLearningMinutes + :minutes WHERE id = 1")
    suspend fun addLearningMinutes(minutes: Int)
}

@Dao
interface CourseEnrollmentDao {
    @Query("SELECT * FROM course_enrollments")
    fun getAllEnrollments(): Flow<List<CourseEnrollmentEntity>>

    @Query("SELECT * FROM course_enrollments WHERE courseId = :courseId LIMIT 1")
    fun getEnrollment(courseId: String): Flow<CourseEnrollmentEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun enroll(enrollment: CourseEnrollmentEntity)

    @Query("UPDATE course_enrollments SET progressPercent = :percent, lastLessonId = :lessonId WHERE courseId = :courseId")
    suspend fun updateProgress(courseId: String, percent: Int, lessonId: String)

    @Query("DELETE FROM course_enrollments WHERE courseId = :courseId")
    suspend fun unenroll(courseId: String)
}

@Dao
interface DownloadedResourceDao {
    @Query("SELECT * FROM downloaded_resources ORDER BY downloadedAt DESC")
    fun getAllDownloads(): Flow<List<DownloadedResourceEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM downloaded_resources WHERE resourceId = :resourceId)")
    fun isDownloaded(resourceId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDownload(resource: DownloadedResourceEntity)

    @Query("DELETE FROM downloaded_resources WHERE resourceId = :resourceId")
    suspend fun deleteDownload(resourceId: String)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM app_notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM app_notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE app_notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("UPDATE app_notifications SET isRead = 1")
    suspend fun markAllAsRead()
}
