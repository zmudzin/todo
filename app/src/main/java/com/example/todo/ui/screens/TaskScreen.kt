package com.example.todo.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.todo.components.AddTaskDialog
import com.example.todo.components.Header
import com.example.todo.components.TaskItem

import com.example.todo.viewmodels.ShoppingListViewModel
@Composable
fun TaskScreen(
    viewModel: ShoppingListViewModel,
    token: String
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchShoppingList(
            token = token,
            onSuccess = { isLoading = false },
            onError = { error ->
                isLoading = false
                errorMessage = error
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDialogOpen = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Dodaj zadanie"
                )
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Header(title = "Twoje zadania")

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    errorMessage != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = errorMessage ?: "Nieznany błąd")
                        }
                    }
                    viewModel.shoppingItems.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Brak pozycji. Kliknij '+' aby dodać nowe.")
                        }
                    }
                    else -> {
                        LazyColumn {
                            items(viewModel.shoppingItems) { item ->
                                TaskItem(
                                    task = item,
                                    onTaskCheckedChange = { isChecked ->
                                        viewModel.toggleItemState(
                                            token = token,
                                            item = item,
                                            onSuccess = {
                                                Log.d("TASK_SCREEN", "Stan elementu zaktualizowany: ${item.name}")
                                            },
                                            onError = { error ->
                                                Log.e("TASK_SCREEN", "Błąd zmiany stanu: $error")
                                            }
                                        )
                                    },
                                    onDelete = {
                                        viewModel.removeItem(
                                            token = token,
                                            itemName = item.name,
                                            onSuccess = {
                                                Log.d("TASK_SCREEN", "Element usunięty: ${item.name}")
                                            },
                                            onError = { error ->
                                                Log.e("TASK_SCREEN", "Błąd usuwania: $error")
                                            }
                                        )
                                    }
                                )

                            }
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
                    viewModel.addItem(
                        token = token,
                        itemName = newTaskName,
                        onSuccess = {
                            isDialogOpen = false
                            Log.d("TASK_SCREEN", "Element dodany: $newTaskName")
                        },
                        onError = { error ->
                            Log.e("TASK_SCREEN", "Błąd dodawania elementu: $error")
                        }
                    )
                }
            }
        )
    }
}
