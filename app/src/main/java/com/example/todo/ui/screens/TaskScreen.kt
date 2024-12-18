// TaskScreen.kt
package com.example.todo.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todo.components.AddTaskDialog
import com.example.todo.components.EditTaskDialog
import com.example.todo.components.ExtendedFAB
import com.example.todo.ui.viewmodels.TaskViewModel
@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFAB(
                onAddTaskClick = viewModel::onAddDialogOpen,
                onDeleteCompletedClick = viewModel::onDeleteCompletedTasks,
                hasCompletedTasks = uiState.completedTasks.isNotEmpty()
            )
        }
    ) { paddingValues ->
        TaskContent(
            activeTasks = uiState.activeTasks,
            completedTasks = uiState.completedTasks,
            onTaskCheckedChange = viewModel::onTaskCheckedChange,
            onDelete = viewModel::onDeleteTask,
            onEdit = viewModel::onEditTask,
            onMove = viewModel::onReorderTasks,
            modifier = Modifier.padding(paddingValues)
        )
    }

    if (uiState.isDialogOpen) {
        AddTaskDialog(
            onDismiss = viewModel::onAddDialogClose,
            onAdd = viewModel::onAddTask
        )
    }

    uiState.editingTask?.let { task ->
        EditTaskDialog(
            initialTaskName = task.name,
            onDismiss = viewModel::onEditDialogClose,
            onEdit = { newName -> viewModel.onTaskEdit(task, newName) }
        )
    }
}