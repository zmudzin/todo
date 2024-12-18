package com.example.todo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todo.components.Header
import com.example.todo.components.TaskItem
import com.example.todo.models.Task

@Composable
fun TaskContent(
    activeTasks: List<Task>,
    completedTasks: List<Task>,
    onTaskCheckedChange: (Task, Boolean) -> Unit,
    onDelete: (Task) -> Unit,
    onEdit: (Task) -> Unit,
    onMove: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (activeTasks.isEmpty() && completedTasks.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Brak zadań",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Kliknij + aby dodać nowe",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Header(title = "Aktywne")
                DraggableTaskList(
                    tasks = activeTasks,
                    onMove = onMove,
                    onTaskCheckedChange = { index, isChecked ->
                        val task = activeTasks[index]
                        onTaskCheckedChange(task, isChecked)
                    },
                    onDelete = { index ->
                        val task = activeTasks[index]
                        onDelete(task)
                    },
                    onTaskEdit = { task -> onEdit(task) }
                )

                if (completedTasks.isNotEmpty()) {
                    Header(title = "Ukończone")
                    LazyColumn {
                        items(completedTasks) { task ->
                            TaskItem(
                                task = task,
                                onTaskCheckedChange = { isChecked ->
                                    onTaskCheckedChange(task, isChecked)
                                },
                                onDelete = { onDelete(task) },
                                onEdit = { onEdit(task) }
                            )
                        }
                    }
                }
            }
        }
    }
}