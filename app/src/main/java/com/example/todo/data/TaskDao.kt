package com.example.todo.data

import androidx.room.*
import com.example.todo.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE isChecked = 0 ORDER BY position ASC")
    fun getActiveTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isChecked = 1 ORDER BY position ASC")
    fun getCompletedTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("UPDATE tasks SET position = :position WHERE id = :taskId")
    suspend fun updateTaskPosition(taskId: String, position: Int)

    @Query("SELECT * FROM tasks WHERE haEntityId = :entityId")
    fun getTasksByHaEntity(entityId: String): Flow<List<Task>>

    @Query("DELETE FROM tasks WHERE haEntityId = :entityId")
    suspend fun deleteTasksByHaEntity(entityId: String)

    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getTaskCount(): Int

    @Query("DELETE FROM tasks WHERE isChecked = 1")
    suspend fun deleteAllCompletedTasks()

    @Query("SELECT COUNT(*) FROM tasks WHERE isChecked = 1")
    suspend fun getCompletedTaskCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<Task>)
    }
