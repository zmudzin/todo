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
        title = { Text("Dodaj nowe zadania") },
        text = {
            TextField(
                value = taskName,
                onValueChange = {
                    taskName = it
                    isError = false
                },
                placeholder = { Text("Wpisz zadania (każda linia to nowe zadanie)") },
                modifier = Modifier.fillMaxWidth(),
                isError = isError,
                supportingText = if (isError) {
                    { Text("Zadanie nie może być puste") }
                } else null,
                minLines = 3
            )
        },
        confirmButton = {
            Button(onClick = {
                val tasks = taskName.split("\n").map { it.trim() }.filter { it.isNotBlank() }
                if (tasks.isEmpty()) {
                    isError = true
                } else {
                    tasks.forEach { task -> onAdd(task) }
                    taskName = ""
                    onDismiss()
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