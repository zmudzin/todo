package com.example.todo.ui.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import com.example.todo.components.AddTaskDialog
import com.example.todo.components.EditTaskDialog
import com.example.todo.components.ExtendedFAB
import com.example.todo.data.TaskRepository
import com.example.todo.models.Task
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding



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

    val hasCompletedTasks by derivedStateOf { completedTasks.isNotEmpty() }

    val onMove: (Int, Int) -> Unit = { fromIndex, toIndex ->
        if (fromIndex in activeTasks.indices && toIndex in activeTasks.indices) {
            scope.launch {
                val updatedTasks = activeTasks.toMutableList()
                val movedTask = updatedTasks.removeAt(fromIndex)
                updatedTasks.add(toIndex, movedTask)

                updatedTasks.forEachIndexed { index, task ->
                    taskRepository.updateTask(task.copy(position = index))
                }
            }
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
                    scope.launch { taskRepository.deleteAllCompletedTasks() }
                },
                hasCompletedTasks = hasCompletedTasks
            )
        },
        content = { paddingValues ->
            TaskContent(
                activeTasks = activeTasks,
                completedTasks = completedTasks,
                onTaskCheckedChange = { task, isChecked ->
                    scope.launch { taskRepository.updateTask(task.copy(isChecked = isChecked)) }
                },
                onDelete = { task ->
                    scope.launch { taskRepository.deleteTask(task) }
                },
                onEdit = { task -> editingTask = task },
                onMove = onMove,
                modifier = Modifier.padding(paddingValues)
            )
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
