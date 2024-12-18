package com.example.todo.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteSweep

@Composable
fun ExtendedFAB(
    onAddTaskClick: () -> Unit, // Funkcja wywoływana przy kliknięciu "Dodaj"
    onDeleteCompletedClick: () -> Unit, // Funkcja wywoływana przy usuwaniu ukończonych zadań
    hasCompletedTasks: Boolean // Informacja, czy są ukończone zadania
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            // Opcja usunięcia ukończonych zadań (tylko gdy są ukończone zadania)
            if (hasCompletedTasks) {
                AnimatedVisibility(
                    visible = isExpanded || hasCompletedTasks,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    FloatingActionButton(
                        onClick = onDeleteCompletedClick,
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = Color.White,
                        modifier = Modifier.size(56.dp),
                        elevation = FloatingActionButtonDefaults.elevation()
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Usuń ukończone zadania"
                        )
                    }
                }
            }

            // Główny FAB
            FloatingActionButton(
                onClick = {
                    onAddTaskClick()
                    isExpanded = !isExpanded // Rozwijanie menu
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.size(56.dp),
                elevation = FloatingActionButtonDefaults.elevation()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = if (isExpanded) "Zamknij menu" else "Dodaj zadanie"
                )
            }
        }
    }
}
