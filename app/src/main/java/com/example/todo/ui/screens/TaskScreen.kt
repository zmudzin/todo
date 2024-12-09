package com.example.todo.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.todo.components.*
import com.example.todo.viewmodels.ShoppingListViewModel
import com.example.todo.models.ShoppingItem

@Composable
fun TaskScreen(viewModel: ShoppingListViewModel, token: String) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var isEditDialogOpen by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<ShoppingItem?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val activeTasks = viewModel.shoppingItems.filter { !it.complete }
    val completedTasks = viewModel.shoppingItems.filter { it.complete }

    LaunchedEffect(Unit) {
        viewModel.fetchShoppingList(
            token = token,
            onSuccess = { isLoading = false },
            onError = { error -> errorMessage = error }
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
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        TaskContent(
            activeTasks = activeTasks,
            completedTasks = completedTasks,
            isLoading = isLoading,
            errorMessage = errorMessage,
            paddingValues = paddingValues,
            onRefresh = {
                isLoading = true
                viewModel.fetchShoppingList(token, { isLoading = false }, { error -> errorMessage = error })
            },
            onEditTask = { taskToEdit = it; isEditDialogOpen = true },
            onDeleteTask = { task -> viewModel.removeItem(token, task.name, {}, {}) },
            onToggleTaskState = { task ->
                viewModel.toggleItemState(token, task, {}, { error -> Log.e("TASK_SCREEN", error) })
            }
        )
    }

    if (isDialogOpen) {
        AnimatedAddTaskDialog(
            isVisible = isDialogOpen,
            onDismiss = { isDialogOpen = false },
            onAdd = { taskName ->
                viewModel.addItem(
                    token = token,
                    itemName = taskName,
                    { isDialogOpen = false },
                    { error -> Log.e("TASK_SCREEN", error) }
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
                    { isEditDialogOpen = false },
                    { error -> Log.e("TASK_SCREEN", error) }
                )
            }
        )
    }
}
