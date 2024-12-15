package com.example.todo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Column(modifier = modifier.fillMaxSize()) {
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
