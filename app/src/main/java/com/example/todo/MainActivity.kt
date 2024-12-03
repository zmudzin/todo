package com.example.todo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo.ui.screens.TaskScreen
import com.example.todo.viewmodels.ShoppingListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MAIN_ACTIVITY", "Rozpoczynam onCreate")

        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJkNjJmMjEwOGU3MGQ0YzBkYWU1YmRkYzE2MWZhYTEwNyIsImlhdCI6MTczMzI0NDI5MCwiZXhwIjoyMDQ4NjA0MjkwfQ.sFoD5oEnhHtvDZ75kyMNe2vbDy_gziGvauZGUt8qn2I"

        setContent {
            Log.d("MAIN_ACTIVITY", "Ustawiam UI")
            MaterialTheme {
                val viewModel: ShoppingListViewModel = viewModel()
                TaskScreen(viewModel = viewModel, token = token)
            }
        }
        Log.d("MAIN_ACTIVITY", "Zakończyłem onCreate")
    }

}



