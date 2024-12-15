package com.example.todo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.todo.components.AddTaskDialog
import com.example.todo.components.AddTaskFAB
import com.example.todo.components.EditTaskDialog
import com.example.todo.components.ExtendedFAB
import com.example.todo.components.Header
import com.example.todo.components.TaskItem
import com.example.todo.data.TaskRepository
import com.example.todo.models.Task
import kotlinx.coroutines.launch

@Composable
fun TaskScreen(taskRepository: TaskRepository) {
    val scope = rememberCoroutineScope()
    var isDialogOpen by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var taskCount by remember { mutableStateOf(0) }

    // Pobieranie liczby zadań
    LaunchedEffect(Unit) {
        scope.launch {
            taskCount = taskRepository.getTaskCount()
        }
    }

    val activeTasks by taskRepository.activeTasks.collectAsState(initial = emptyList())
    val completedTasks by taskRepository.completedTasks.collectAsState(initial = emptyList())

    val onMove: (Int, Int) -> Unit = { fromIndex, toIndex ->
        if (fromIndex in activeTasks.indices && toIndex in activeTasks.indices) {
            scope.launch {
                val updatedTasks = activeTasks.toMutableList()
                val movedTask = updatedTasks.removeAt(fromIndex)
                updatedTasks.add(toIndex, movedTask)

                // Aktualizacja pozycji w bazie danych
                updatedTasks.forEachIndexed { index, task ->
                    taskRepository.updateTask(task.copy(position = index))
                }
            }
        }
    }

    val onTaskCheckedChange: (Task, Boolean) -> Unit = { task, isChecked ->
        scope.launch {
            taskRepository.updateTask(task.copy(isChecked = isChecked))
        }
    }

    val onDelete: (Task) -> Unit = { task ->
        scope.launch {
            taskRepository.deleteTask(task)
        }
    }

    val onTaskEdit: (Task, String) -> Unit = { task, newName ->
        scope.launch {
            taskRepository.updateTask(task.copy(name = newName))
        }
    }


    Scaffold(
        floatingActionButton = {
            ExtendedFAB(
                onAddTaskClick = { isDialogOpen = true },
                onDeleteCompletedClick = {
                    scope.launch {
                        taskRepository.deleteAllCompletedTasks()
                    }
                },
                hasCompletedTasks = completedTasks.isNotEmpty() // Sprawdzanie, czy są ukończone zadania
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Header(title = "Aktywne")
                DraggableTaskList(
                    tasks = activeTasks,
                    onMove = { from, to -> onMove(from, to) },
                    onTaskCheckedChange = { index, isChecked ->
                        val task = activeTasks[index]
                        onTaskCheckedChange(task, isChecked)
                    },
                    onDelete = { index ->
                        val task = activeTasks[index]
                        onDelete(task)
                    },
                    onTaskEdit = { task ->
                        editingTask = task
                    }
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
                                onEdit = {
                                    editingTask = task
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
                    scope.launch {
                        taskRepository.addTask(Task(name = newTaskName, position = taskCount))
                        taskCount += 1
                    }
                    isDialogOpen = false
                }
            }
        )
    }
    if (editingTask != null) {
        EditTaskDialog(
            initialTaskName = editingTask!!.name,
            onDismiss = { editingTask = null },
            onEdit = { newName ->
                onTaskEdit(editingTask!!, newName)
                editingTask = null
            }
        )
    }

}
