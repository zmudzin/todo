package com.example.todo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.TaskRepository
import com.example.todo.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    // reszta kodu pozostaje bez zmian
    private val _uiState = MutableStateFlow<TaskUiState>(TaskUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            try {
                combine(
                    repository.activeTasks,
                    repository.completedTasks
                ) { active, completed ->
                    TaskUiState.Success(
                        activeTasks = active,
                        completedTasks = completed,
                        taskCount = active.size + completed.size
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = TaskUiState.Error(
                    message = "Nie udało się załadować zadań: ${e.localizedMessage}",
                    cause = e
                )
            }
        }
    }

    fun retryLoading() {
        _uiState.value = TaskUiState.Loading
        loadTasks()
    }

    fun onAddTask(taskName: String) {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value as? TaskUiState.Success ?: return@launch
                val newTask = Task(
                    name = taskName,
                    position = currentState.activeTasks.size
                )
                repository.addTask(newTask)
                _uiState.value = currentState.copy(
                    isDialogOpen = false
                )
            } catch (e: Exception) {
                _uiState.value = TaskUiState.Error("Nie udało się dodać zadania", e)
            }
        }
    }

    fun onReorderTasks(fromIndex: Int, toIndex: Int) {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value as? TaskUiState.Success ?: return@launch
                val tasks = currentState.activeTasks.toMutableList()
                if (fromIndex in tasks.indices && toIndex in tasks.indices) {
                    val movedTask = tasks.removeAt(fromIndex)
                    tasks.add(toIndex, movedTask)
                    tasks.forEachIndexed { index, task ->
                        repository.updateTask(task.copy(position = index))
                    }
                }
            } catch (e: Exception) {
                _uiState.value = TaskUiState.Error("Nie udało się zmienić kolejności zadań", e)
            }
        }
    }

    fun onTaskCheckedChange(task: Task, isChecked: Boolean) {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value as? TaskUiState.Success ?: return@launch
                repository.updateTask(task.copy(isChecked = isChecked))
            } catch (e: Exception) {
                _uiState.value = TaskUiState.Error("Nie udało się zaktualizować stanu zadania", e)
            }
        }
    }

    fun onDeleteTask(task: Task) {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value as? TaskUiState.Success ?: return@launch
                repository.deleteTask(task)
            } catch (e: Exception) {
                _uiState.value = TaskUiState.Error("Nie udało się usunąć zadania", e)
            }
        }
    }

    fun onTaskEdit(task: Task, newName: String) {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value as? TaskUiState.Success ?: return@launch
                repository.updateTask(task.copy(name = newName))
                _uiState.value = currentState.copy(editingTask = null)
            } catch (e: Exception) {
                _uiState.value = TaskUiState.Error("Nie udało się edytować zadania", e)
            }
        }
    }

    fun onDeleteCompletedTasks() {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value as? TaskUiState.Success ?: return@launch
                repository.deleteAllCompletedTasks()
            } catch (e: Exception) {
                _uiState.value = TaskUiState.Error("Nie udało się usunąć ukończonych zadań", e)
            }
        }
    }

    fun onAddDialogOpen() {
        val currentState = _uiState.value as? TaskUiState.Success ?: return
        _uiState.value = currentState.copy(isDialogOpen = true)
    }

    fun onAddDialogClose() {
        val currentState = _uiState.value as? TaskUiState.Success ?: return
        _uiState.value = currentState.copy(isDialogOpen = false)
    }

    fun onEditDialogClose() {
        val currentState = _uiState.value as? TaskUiState.Success ?: return
        _uiState.value = currentState.copy(editingTask = null)
    }

    fun onEditTask(task: Task) {
        val currentState = _uiState.value as? TaskUiState.Success ?: return
        _uiState.value = currentState.copy(editingTask = task)
    }
}