package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CompletedLessonEntity::class,
        BookmarkEntity::class,
        QuizResultEntity::class,
        UserProfileEntity::class,
        CourseEnrollmentEntity::class,
        DownloadedResourceEntity::class,
        NotificationEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun completedLessonDao(): CompletedLessonDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun quizResultDao(): QuizResultDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun courseEnrollmentDao(): CourseEnrollmentDao
    abstract fun downloadedResourceDao(): DownloadedResourceDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "learn_easy_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
