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
            Text(
                text = "Edytuj zadanie",
                color = MaterialTheme.colorScheme.onSurface, // Kolor tekstu z motywu
                style = MaterialTheme.typography.titleLarge // Styl nagłówka
            )
        },
        text = {
            TextField(
                value = taskName,
                onValueChange = { taskName = it },
                placeholder = {
                    Text(
                        text = "Zmień treść zadania",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f) // Kolor placeholdera
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (taskName.isNotBlank()) {
                        onSave(taskName)
                    } else {
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text("Anuluj")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface // Tło dialogu
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
