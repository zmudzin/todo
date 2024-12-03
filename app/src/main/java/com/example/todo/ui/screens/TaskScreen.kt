package com.example.todo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todo.components.AddTaskDialog
import com.example.todo.components.Header
import com.example.todo.components.TaskItem
import com.example.todo.models.Task

@Composable
fun TaskScreen() {
    // Lista zadań w pamięci
    var tasks by remember {
        mutableStateOf(
            listOf(
                Task("Zadanie 1"),
                Task("Zadanie 2"),
                Task("Zadanie 3")
            )
        )
    }
    var isDialogOpen by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDialogOpen = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Dodaj zadanie"
                )
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Header(title = "Twoje zadania")

                if (tasks.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Brak zadań. Kliknij '+' aby dodać nowe.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn {
                        items(tasks) { task ->
                            TaskItem(
                                task = task,
                                onTaskCheckedChange = { isChecked ->
                                    tasks = tasks.map {
                                        if (it == task) it.copy(isChecked = isChecked) else it
                                    }
                                },
                                onDelete = {
                                    tasks = tasks.filter { it != task }
                                }
                            )
                        }
                    }
                }
            }
        }
    )

    if (isDialogOpen) {
        AddTaskDialog(
            onDismiss = { isDialogOpen = false },
            onAdd = { newTaskName ->
                if (newTaskName.isNotBlank()) {
                    tasks = tasks + Task(newTaskName)
                }
                isDialogOpen = false
            }
        )
    }
}
