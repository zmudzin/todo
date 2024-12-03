package com.example.todo.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todo.components.AddTaskDialog
import com.example.todo.components.Header
import com.example.todo.components.TaskItem

import com.example.todo.viewmodels.ShoppingListViewModel

@Composable
fun TaskScreen(
    viewModel: ShoppingListViewModel,
    token: String
) {
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) } // Dodano brakującą zmienną

    // Sprawdzamy token na początku
    if (token.isBlank()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Brak tokena. Podaj poprawny token w kodzie.")
        }
        return
    }

    // Pobieranie danych w LaunchedEffect
    LaunchedEffect(Unit) {
        try {
            viewModel.fetchShoppingList(token, {
                isLoading = false
            }, { error ->
                isLoading = false
                errorMessage = error
            })
        } catch (e: Exception) {
            isLoading = false
            errorMessage = "Exception: ${e.localizedMessage}"
            Log.e("TASK_SCREEN", "Wystąpił wyjątek: ${e.localizedMessage}", e)
        }
    }





    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDialogOpen = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
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
                            Text(
                                text = errorMessage ?: "Nieznany błąd",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    viewModel.shoppingItems.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Brak pozycji. Kliknij '+' aby dodać nowe.",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    else -> {
                        LazyColumn {
                            items(viewModel.shoppingItems) { item ->
                                TaskItem(
                                    task = item, // Bez konwersji
                                    onTaskCheckedChange = { isChecked ->
                                        item.complete = isChecked // Aktualizujemy pole `complete` bezpośrednio
                                    },
                                    onDelete = {
                                        viewModel.shoppingItems.remove(item) // Usuwamy element z listy
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
                    // Dodaj do Home Assistant (do zaimplementowania)
                    isDialogOpen = false
                }
            }
        )
    }
}
