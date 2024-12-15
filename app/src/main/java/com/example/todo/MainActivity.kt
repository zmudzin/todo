package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.todo.ui.theme.ToDoAppTheme
import com.example.todo.ui.screens.TaskScreen
import com.example.todo.data.DatabaseProvider
import com.example.todo.data.TaskRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicjalizacja bazy danych i repozytorium
        val database = DatabaseProvider.getDatabase(this)
        val taskRepository = TaskRepository(database.taskDao())

        setContent {
            ToDoAppTheme {
                TaskScreen(taskRepository = taskRepository)
            }
        }
    }
}
