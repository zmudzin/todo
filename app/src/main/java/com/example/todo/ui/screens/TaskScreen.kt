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

    // Podział zadań na aktywne i ukończone
    val activeTasks = viewModel.shoppingItems.filter { !it.complete }
    val completedTasks = viewModel.shoppingItems.filter { it.complete }

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
                    .size(88.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(50))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
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
                            // Sekcja aktywnych zadań
                            if (activeTasks.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "Aktywne",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                                items(activeTasks) { item ->
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

                            // Sekcja ukończonych zadań
                            if (completedTasks.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "Ukończone",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                                items(completedTasks) { item ->
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
        }
    )

    // Obsługa dialogu dodawania
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

    // Obsługa dialogu edycji
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