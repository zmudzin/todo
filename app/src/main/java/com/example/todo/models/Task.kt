package com.example.todo.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val isChecked: Boolean = false,
    val position: Int,
    val haEntityId: String? = null, // ID encji z HA
    val lastSyncedState: String? = null // Stan synchronizacji z HA
)