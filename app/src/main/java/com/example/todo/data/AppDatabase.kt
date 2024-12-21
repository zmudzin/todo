package com.example.todo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo.models.HAEntity
import com.example.todo.models.Task

@Database(
    entities = [Task::class, HAEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun haEntityDao(): HAEntityDao
}