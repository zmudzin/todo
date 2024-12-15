package com.example.todo.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val isChecked: Boolean = false,
    val position: Int // Przechowywanie kolejności
)
