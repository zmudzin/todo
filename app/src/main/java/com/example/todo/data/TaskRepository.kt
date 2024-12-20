package com.example.todo.data

import com.example.todo.models.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    val activeTasks: Flow<List<Task>> = taskDao.getActiveTasks()
    val completedTasks: Flow<List<Task>> = taskDao.getCompletedTasks()

    suspend fun addTask(task: Task) = taskDao.insertTask(task)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
    suspend fun updateTaskPosition(taskId: String, position: Int) = taskDao.updateTaskPosition(taskId, position)
    suspend fun getTaskCount(): Int = taskDao.getTaskCount()
    suspend fun deleteAllCompletedTasks() { taskDao.deleteAllCompletedTasks() }
    suspend fun getCompletedTaskCount(): Int = taskDao.getCompletedTaskCount()
}