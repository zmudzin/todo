package com.example.todo.models

data class Task(
    val name: String,
    var isChecked: Boolean = false // Default value for checkbox
)