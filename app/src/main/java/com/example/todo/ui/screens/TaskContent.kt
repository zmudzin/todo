package com.example.todo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.todo.components.Header
import com.example.todo.models.ShoppingItem

@Composable
fun TaskContent(
    activeTasks: List<ShoppingItem>,
    completedTasks: List<ShoppingItem>,
    isLoading: Boolean,
    errorMessage: String?,
    paddingValues: PaddingValues,
    onRefresh: () -> Unit,
    onEditTask: (ShoppingItem) -> Unit,
    onDeleteTask: (ShoppingItem) -> Unit,
    onToggleTaskState: (ShoppingItem) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        Header(title = "Twoje zadania", onRefresh = onRefresh)

        when {
            isLoading -> {
                LoadingIndicator()
            }
            errorMessage != null -> {
                ErrorMessage(message = errorMessage)
            }
            activeTasks.isEmpty() && completedTasks.isEmpty() -> {
                EmptyListMessage()
            }
            else -> {
                LazyColumn {
                    taskSection("Aktywne", activeTasks, onEditTask, onDeleteTask, onToggleTaskState)
                    taskSection("Ukończone", completedTasks, onEditTask, onDeleteTask, onToggleTaskState)
                }
            }
        }
    }
}
