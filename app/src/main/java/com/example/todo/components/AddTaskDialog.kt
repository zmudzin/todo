package com.example.todo.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var taskName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Dodaj nowe zadanie") // Wygląd tekstu przejdzie przez `theme`
        },
        text = {
            TextField(
                value = taskName,
                onValueChange = { taskName = it },
                placeholder = { Text("Wpisz nazwę zadania") }, // Wygląd placeholdera również przez `theme`
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = {
                onAdd(taskName)
                taskName = ""
            }) {
                Text("Dodaj") // Wygląd tekstu przycisku przejdzie przez `theme`
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Anuluj") // Wygląd tekstu przycisku przejdzie przez `theme`
            }
        }
    )
}
    @Composable
    fun AnimatedAddTaskDialog(
        isVisible: Boolean,
        onDismiss: () -> Unit,
        onAdd: (String) -> Unit
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            AddTaskDialog(onDismiss = onDismiss, onAdd = onAdd)
        }
    }
