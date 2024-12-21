package com.example.todo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todo.models.HAEntity
import com.example.todo.models.Task

@Database(
    entities = [Task::class, HAEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun haEntityDao(): HAEntityDao

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE tasks ADD COLUMN haEntityId TEXT")
                database.execSQL("ALTER TABLE tasks ADD COLUMN lastSyncedState TEXT")
            }
        }
    }
}