// TaskScreen.kt
package com.example.todo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.todo.components.AddTaskDialog
import com.example.todo.components.EditTaskDialog
import com.example.todo.components.ExtendedFAB
import com.example.todo.ui.viewmodels.TaskViewModel
import com.example.todo.ui.viewmodels.TaskUiState

@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is TaskUiState.Loading -> {
            CircularProgressIndicator()
        }

        is TaskUiState.Error -> {
            val error = uiState as TaskUiState.Error
            ErrorContent(
                message = error.message,
                onRetry = viewModel::retryLoading
            )
        }

        is TaskUiState.Success -> {
            val successState = uiState as TaskUiState.Success
            MainContent(
                uiState = successState,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Button(onClick = onRetry) {
            Text("Spróbuj ponownie")
        }
    }
}

@Composable
private fun MainContent(
    uiState: TaskUiState.Success,
    viewModel: TaskViewModel
) {
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
}