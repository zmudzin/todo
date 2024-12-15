package com.example.todo.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun EditTaskDialog(
    initialTaskName: String,
    onDismiss: () -> Unit,
    onEdit: (String) -> Unit
) {
    var taskName by remember { mutableStateOf(initialTaskName) }

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(text = "Edytuj zadanie")
        },
        text = {
            TextField(
                value = taskName,
                onValueChange = { taskName = it },
                placeholder = { Text("Edytuj treść zadania") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (taskName.isNotBlank()) {
                        onEdit(taskName)
                        onDismiss()
                    }
                }
            ) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}
