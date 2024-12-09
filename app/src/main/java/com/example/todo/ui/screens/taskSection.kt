package com.example.todo.ui.screens

import AnimatedTaskItem
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import com.example.todo.models.ShoppingItem

fun LazyListScope.taskSection(
    title: String,
    tasks: List<ShoppingItem>,
    onEditTask: (ShoppingItem) -> Unit,
    onDeleteTask: (ShoppingItem) -> Unit,
    onToggleTaskState: (ShoppingItem) -> Unit
) {
    if (tasks.isNotEmpty()) {
        item {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
        items(tasks) { task ->
            AnimatedTaskItem(
                task = task,
                onTaskCheckedChange = { onToggleTaskState(task) },
                onDelete = { onDeleteTask(task) },
                onEdit = { onEditTask(task) }
            )
        }
    }
}
