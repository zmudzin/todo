package com.example.todo.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import com.example.todo.components.*
import com.example.todo.viewmodels.ShoppingListViewModel
import com.example.todo.models.ShoppingItem
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.lazy.LazyColumn

@Composable
fun TaskScreen(viewModel: ShoppingListViewModel, token: String) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var isEditDialogOpen by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<ShoppingItem?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showRefreshButton by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredActiveTasks = viewModel.shoppingItems.filter {
        it.name.contains(searchQuery, ignoreCase = true) && !it.complete
    }
    val filteredCompletedTasks = viewModel.shoppingItems.filter {
        it.name.contains(searchQuery, ignoreCase = true) && it.complete
    }

    LaunchedEffect(Unit) {
        viewModel.fetchShoppingList(
            token = token,
            onSuccess = { isLoading = false },
            onError = { error -> errorMessage = error }
        )
    }

    Scaffold(
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
            ) {
                if (showRefreshButton) {
                    FloatingActionButton(
                        onClick = {
                            isLoading = true
                            viewModel.fetchShoppingList(
                                token = token,
                                onSuccess = { isLoading = false },
                                onError = { error -> errorMessage = error }
                            )
                            showRefreshButton = false
                        },
                        modifier = Modifier
                            .size(58.dp)
                            .clip(RoundedCornerShape(50)),
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Odśwież")
                    }
                }

                FloatingActionButton(
                    onClick = { isDialogOpen = true },
                    modifier = Modifier
                        .size(58.dp)
                        .clip(RoundedCornerShape(50))
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onVerticalDrag = { _, dragAmount ->
                                    if (dragAmount < -50) {
                                        showRefreshButton = true
                                    }
                                }
                            )
                        },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Dodaj zadanie")
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Header(
                onSearch = { query -> searchQuery = query },
                onRefresh = {
                    isLoading = true
                    viewModel.fetchShoppingList(
                        token,
                        onSuccess = { isLoading = false },
                        onError = { error -> errorMessage = error }
                    )
                }
            )

            when {
                isLoading -> {
                    LoadingIndicator()
                }
                errorMessage != null -> {
                    ErrorMessage(message = errorMessage ?: "Unknown error occurred")
                }
                filteredActiveTasks.isEmpty() && filteredCompletedTasks.isEmpty() -> {
                    EmptyListMessage()
                }
                else -> {
                    LazyColumn {
                        taskSection("Aktywne", filteredActiveTasks, {
                            taskToEdit = it; isEditDialogOpen = true
                        }, {
                            viewModel.removeItem(token, it.name, {}, {})
                        }, {
                            viewModel.toggleItemState(
                                token,
                                it,
                                onSuccess = {},
                                onError = { error -> Log.e("TASK_SCREEN", error) }
                            )
                        })
                        taskSection("Ukończone", filteredCompletedTasks, {
                            taskToEdit = it; isEditDialogOpen = true
                        }, {
                            viewModel.removeItem(token, it.name, {}, {})
                        }, {
                            viewModel.toggleItemState(
                                token,
                                it,
                                onSuccess = {},
                                onError = { error -> Log.e("TASK_SCREEN", error) }
                            )
                        })
                    }
                }
            }
        }
    }

    if (isDialogOpen) {
        AnimatedAddTaskDialog(
            isVisible = isDialogOpen,
            onDismiss = { isDialogOpen = false },
            onAdd = { taskName ->
                viewModel.addItem(
                    token = token,
                    itemName = taskName,
                    onSuccess = { isDialogOpen = false },
                    onError = { error -> Log.e("TASK_SCREEN", error) }
                )
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
                    onSuccess = { isEditDialogOpen = false },
                    onError = { error -> Log.e("TASK_SCREEN", error) }
                )
            }
        )
    }
}
