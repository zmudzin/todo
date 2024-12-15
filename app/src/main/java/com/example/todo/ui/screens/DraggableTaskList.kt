package com.example.todo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.example.todo.components.DragController
import com.example.todo.models.Task
import com.example.todo.components.TaskItem

@Composable
fun DraggableTaskList(
    tasks: List<Task>, // Lista aktywnych zadań
    onMove: (Int, Int) -> Unit, // Funkcja zmiany kolejności
    onTaskCheckedChange: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit,
    onTaskEdit: (Task) -> Unit
) {
    val dragController = remember { DragController(tasks, onMove) }
    val itemHeight = remember { mutableStateOf(0f) }

    LazyColumn {
        itemsIndexed(tasks) { index, task ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        if (itemHeight.value == 0f) {
                            itemHeight.value = coordinates.size.height.toFloat()
                        }
                    }
                    .then(dragController.dragModifier(index, itemHeight.value, tasks.size))
            ) {
                Icon(
                    imageVector = Icons.Default.DragHandle,
                    contentDescription = "Przeciągnij",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                )

                TaskItem(
                    task = task,
                    onTaskCheckedChange = { isChecked -> onTaskCheckedChange(index, isChecked) },
                    onDelete = { onDelete(index) },
                    onEdit = { onTaskEdit(task) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
