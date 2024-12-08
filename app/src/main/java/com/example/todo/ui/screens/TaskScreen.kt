package com.example.todo.ui.screens

import AnimatedTaskItem
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.todo.components.AnimatedAddTaskDialog
import com.example.todo.components.AnimatedEditTaskDialog
import com.example.todo.components.EditTaskDialog
import com.example.todo.components.Header
import com.example.todo.models.ShoppingItem
import com.example.todo.viewmodels.ShoppingListViewModel

@Composable
fun TaskScreen(
    viewModel: ShoppingListViewModel,
    token: String
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var isEditDialogOpen by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<ShoppingItem?>(null) }
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
                modifier = Modifier
                    .size(88.dp) // Rozmiar przycisku
                    .padding(16.dp) // Odstęp od krawędzi ekranu
                    .clip(RoundedCornerShape(50)) // Wymuszony okrągły kształt
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null // Treść dla dostępności
                )
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Padding kontrolowany przez Scaffold
            ) {
                Header(
                    title = "Twoje zadania",
                    onRefresh = {
                        isLoading = true
                        viewModel.fetchShoppingList(
                            token = token,
                            onSuccess = { isLoading = false },
                            onError = { error ->
                                isLoading = false
                                errorMessage = error
                            }
                        )
                    }
                )

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center // Wyrównanie centralne przez Alignment
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
                                AnimatedTaskItem(
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
                                    },
                                    onEdit = {
                                        taskToEdit = item
                                        isEditDialogOpen = true
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
        AnimatedAddTaskDialog(
            isVisible = isDialogOpen,
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

    if (isEditDialogOpen && taskToEdit != null) {
        AnimatedEditTaskDialog(
            isVisible = isEditDialogOpen,
            currentName = taskToEdit!!.name,
            onDismiss = { isEditDialogOpen = false },
            onSave = { newName ->
                viewModel.editItem(
                    token = token,
                    oldName = taskToEdit!!.name,
                    newName = newName,
                    onSuccess = {
                        isEditDialogOpen = false
                        Log.d("TASK_SCREEN", "Zadanie zaktualizowane: $newName")
                    },
                    onError = { error ->
                        Log.e("TASK_SCREEN", "Błąd edycji zadania: $error")
                    }
                )
            }
        )
    }
}
