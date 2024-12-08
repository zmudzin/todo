package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.todo.network.ApiClient
import com.example.todo.repository.ShoppingRepository
import com.example.todo.ui.screens.TaskScreen
import com.example.todo.viewmodels.ShoppingListViewModel
import com.example.todo.viewmodels.ShoppingViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { // Poprawiony brak 'savedInstanceState'
        super.onCreate(savedInstanceState)

        // Inicjalizacja repository i viewmodel
        val repository = ShoppingRepository(ApiClient.api)
        val viewModel: ShoppingListViewModel = ViewModelProvider(
            this,
            ShoppingViewModelFactory(repository)
        )[ShoppingListViewModel::class.java]

        // Ustawienie UI
        setContent {
            TaskScreen(viewModel = viewModel, token = BuildConfig.HA_TOKEN)
        }
    }
}
