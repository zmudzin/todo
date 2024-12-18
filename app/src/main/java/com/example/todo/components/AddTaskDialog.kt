package com.example.todo.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var taskName by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dodaj nowe zadanie") },
        text = {
            TextField(
                value = taskName,
                onValueChange = {
                    taskName = it
                    isError = false
                },
                placeholder = { Text("Wpisz nazwę zadania") },
                modifier = Modifier.fillMaxWidth(),
                isError = isError,
                supportingText = if (isError) {
                    { Text("Zadanie nie może być puste") }
                } else null
            )
        },
        confirmButton = {
            Button(onClick = {
                if (taskName.trim().isBlank()) {
                    isError = true
                } else {
                    onAdd(taskName.trim())
                    taskName = ""
                }
            }) {
                Text("Dodaj")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}