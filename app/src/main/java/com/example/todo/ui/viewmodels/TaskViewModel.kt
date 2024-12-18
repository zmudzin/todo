// TaskViewModel.kt
package com.example.todo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.TaskRepository
import com.example.todo.models.Task
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.activeTasks.collect { tasks ->
                _uiState.update { it.copy(activeTasks = tasks) }
            }
        }
        viewModelScope.launch {
            repository.completedTasks.collect { tasks ->
                _uiState.update { it.copy(completedTasks = tasks) }
            }
        }
    }

    fun onAddTask(taskName: String) {
        viewModelScope.launch {
            repository.addTask(Task(name = taskName, position = _uiState.value.taskCount))
            _uiState.update { it.copy(isDialogOpen = false, taskCount = it.taskCount + 1) }
        }
    }

    fun onReorderTasks(fromIndex: Int, toIndex: Int) {
        val tasks = _uiState.value.activeTasks
        if (fromIndex in tasks.indices && toIndex in tasks.indices) {
            viewModelScope.launch {
                val updatedTasks = tasks.toMutableList()
                val movedTask = updatedTasks.removeAt(fromIndex)
                updatedTasks.add(toIndex, movedTask)
                updatedTasks.forEachIndexed { index, task ->
                    repository.updateTask(task.copy(position = index))
                }
            }
        }
    }

    fun onTaskCheckedChange(task: Task, isChecked: Boolean) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isChecked = isChecked))
        }
    }

    fun onDeleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun onTaskEdit(task: Task, newName: String) {
        viewModelScope.launch {
            repository.updateTask(task.copy(name = newName))
            _uiState.update { it.copy(editingTask = null) }
        }
    }

    fun onDeleteCompletedTasks() {
        viewModelScope.launch {
            repository.deleteAllCompletedTasks()
        }
    }

    fun onAddDialogOpen() = _uiState.update { it.copy(isDialogOpen = true) }
    fun onAddDialogClose() = _uiState.update { it.copy(isDialogOpen = false) }
    fun onEditDialogClose() = _uiState.update { it.copy(editingTask = null) }
    fun onEditTask(task: Task) = _uiState.update { it.copy(editingTask = task) }
}
data class TaskUiState(
    val activeTasks: List<Task> = emptyList(),
    val completedTasks: List<Task> = emptyList(),
    val isDialogOpen: Boolean = false,
    val editingTask: Task? = null,
    val taskCount: Int = 0
)