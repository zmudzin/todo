package com.example.todo.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todo.models.ShoppingItem

@Composable
fun TaskItem(
    task: ShoppingItem,
    onTaskCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.complete,
            onCheckedChange = { isChecked ->
                Log.d("TASK_ITEM", "Checkbox clicked for ${task.name}: new state=$isChecked")
                onTaskCheckedChange(isChecked)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = task.name,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Usuń")
        }
    }
}




