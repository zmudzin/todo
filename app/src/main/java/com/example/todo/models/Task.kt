package com.example.todo.models

data class Task(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "Nazwa zadania",
    var isChecked: Boolean = false // Default value for checkbox
)