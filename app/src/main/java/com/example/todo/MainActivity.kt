package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.todo.ui.theme.ToDoAppTheme
import com.example.todo.ui.screens.TaskScreen
import com.example.todo.data.DatabaseProvider
import com.example.todo.data.TaskRepository
import com.example.todo.ui.viewmodels.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = DatabaseProvider.getDatabase(this)
        val repository = TaskRepository(database.taskDao())
        val viewModel = TaskViewModel(repository)

        setContent {
            ToDoAppTheme {
                TaskScreen(viewModel)
            }
        }
    }
}