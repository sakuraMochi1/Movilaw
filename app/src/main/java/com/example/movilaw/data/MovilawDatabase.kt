package com.example.movilaw.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movilaw.model.StudyNote
import com.example.movilaw.model.User

@Database(
    entities = [User::class, StudyNote::class],
    version = 1,
    exportSchema = false
)
abstract class MovilawDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun studyNoteDao(): StudyNoteDao

    companion object {
        @Volatile
        private var INSTANCE: MovilawDatabase? = null

        fun getDatabase(context: Context): MovilawDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovilawDatabase::class.java,
                    "movilaw_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}