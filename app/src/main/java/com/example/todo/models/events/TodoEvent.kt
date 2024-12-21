package com.example.todo.models.events

import com.example.todo.models.Task

sealed class TodoEvent {
    data class ItemsUpdated(val items: List<Task>) : TodoEvent()
    data class Error(val exception: Exception) : TodoEvent()
}