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
fun EditTaskDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var taskName by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Edytuj zadanie") // Wygląd tytułu zarządzany przez `theme`
        },
        text = {
            TextField(
                value = taskName,
                onValueChange = { taskName = it },
                placeholder = {
                    Text("Zmień treść zadania") // Wygląd placeholdera zarządzany przez `theme`
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = {
                if (taskName.isNotBlank()) {
                    onSave(taskName) // Przekazuje zmienioną nazwę
                } else {
                    onDismiss() // Zamknij dialog, jeśli pole jest puste
                }
            }) {
                Text("Zapisz") // Wygląd tekstu przycisku zarządzany przez `theme`
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Anuluj") // Wygląd tekstu przycisku zarządzany przez `theme`
            }
        }

    )

}
@Composable
fun AnimatedEditTaskDialog(
    isVisible: Boolean,
    currentName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        EditTaskDialog(
            currentName = currentName,
            onDismiss = onDismiss,
            onSave = onSave
        )
    }
}