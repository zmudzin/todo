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
import com.example.todo.components.EditTaskDialog
import com.example.todo.components.Header
import com.example.todo.components.TaskItem
import com.example.todo.models.Task

@Composable
fun TaskScreen() {
    var tasks by remember {
        mutableStateOf(
            listOf(
                Task(name = "Zadanie 1"),
                Task(name = "Zadanie 2"),
                Task(name = "Zadanie 3"),
                Task(name = "Zadanie 4"),
                Task(name = "Zadanie 5")
            )
        )
    }
    var isDialogOpen by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }

    val activeTasks = tasks.filter { !it.isChecked }
    val completedTasks = tasks.filter { it.isChecked }

    // Funkcja do zmiany pozycji zadań
    val onMove: (Int, Int) -> Unit = { fromIndex, toIndex ->
        val activeTasks = tasks.filter { !it.isChecked } // Lokalna lista aktywnych zadań
        val globalFromIndex = tasks.indexOf(activeTasks[fromIndex]) // Mapowanie indeksów
        val globalToIndex = tasks.indexOf(activeTasks[toIndex])

        tasks = tasks.toMutableList().apply {
            val item = removeAt(globalFromIndex)
            add(globalToIndex, item)
        }
    }

    // Funkcja do zmiany stanu checkboxa
    val onTaskCheckedChange: (Int, Boolean) -> Unit = { index, isChecked ->
        tasks = tasks.toMutableList().apply {
            this[index] = this[index].copy(isChecked = isChecked)
        }
    }

    // Funkcja do usuwania zadania
    val onDelete: (Int) -> Unit = { index ->
        tasks = tasks.toMutableList().apply {
            removeAt(index)
        }
    }

    // Funkcja do edycji zadania
    val onTaskEdit: (Task) -> Unit = { task ->
        editingTask = task
    }

    // Funkcja wywoływana po zapisaniu zmian w edytowanym zadaniu
    val onEdit: (String) -> Unit = { newName ->
        editingTask?.let { task ->
            tasks = tasks.map {
                if (it.id == task.id) it.copy(name = newName) else it
            }
        }
        editingTask = null
    }

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
                // Sekcja aktywnych zadań
                Header(title = "Aktywne")
                DraggableTaskList(
                    tasks = activeTasks,
                    onMove = onMove,
                    onTaskCheckedChange = { index, isChecked ->
                        val globalIndex = tasks.indexOf(activeTasks[index])
                        onTaskCheckedChange(globalIndex, isChecked)
                    },
                    onDelete = { index ->
                        val globalIndex = tasks.indexOf(activeTasks[index])
                        onDelete(globalIndex)
                    },
                    onTaskEdit = { task ->
                        onTaskEdit(task)
                    }
                )

                // Sekcja ukończonych zadań (jeśli istnieją)
                if (completedTasks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Header(title = "Ukończone")
                    LazyColumn {
                        items(completedTasks) { task ->
                            TaskItem(
                                task = task,
                                onTaskCheckedChange = { isChecked ->
                                    val globalIndex = tasks.indexOf(task)
                                    onTaskCheckedChange(globalIndex, isChecked)
                                },
                                onDelete = {
                                    val globalIndex = tasks.indexOf(task)
                                    onDelete(globalIndex)
                                },
                                onEdit = { onTaskEdit(task) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    )

    // Dialog dodawania zadania
    if (isDialogOpen) {
        AddTaskDialog(
            onDismiss = { isDialogOpen = false },
            onAdd = { newTaskName ->
                if (newTaskName.isNotBlank()) {
                    tasks = tasks + Task(name = newTaskName)
                }
                isDialogOpen = false
            }
        )
    }

    // Dialog edycji zadania
    editingTask?.let { task ->
        EditTaskDialog(
            initialTaskName = task.name,
            onDismiss = { editingTask = null },
            onEdit = onEdit
        )
    }
}
