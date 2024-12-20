package com.example.todo.ui.viewmodels

import com.example.todo.models.Task

sealed interface TaskUiState {
    object Loading : TaskUiState

    data class Success(
        val activeTasks: List<Task> = emptyList(),
        val completedTasks: List<Task> = emptyList(),
        val isDialogOpen: Boolean = false,
        val editingTask: Task? = null,
        val taskCount: Int = 0
    ) : TaskUiState

    data class Error(
        val message: String,
        val cause: Throwable? = null
    ) : TaskUiState
}