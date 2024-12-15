package com.example.todo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo.models.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
