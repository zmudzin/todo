import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todo.models.ShoppingItem

@Composable
fun AnimatedTaskItem(
    task: ShoppingItem,
    onTaskCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onEdit: (String) -> Unit
) {
    // Zmienne stanu
    var isVisible by remember { mutableStateOf(true) }

    // Animowana widoczność
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        TaskItem(
            task = task,
            onTaskCheckedChange = onTaskCheckedChange,
            onDelete = {
                isVisible = false // Ukrycie przed wywołaniem onDelete
                onDelete()
            },
            onEdit = onEdit
        )
    }
}

@Composable
fun TaskItem(
    task: ShoppingItem,
    onTaskCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onEdit: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable { onEdit(task.name) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox po lewej stronie
        Checkbox(
            checked = task.complete,
            onCheckedChange = onTaskCheckedChange,
            modifier = Modifier.padding(end = 8.dp)
        )

        // Tekst zadania
        Text(
            text = task.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Ikona kosza po prawej stronie
        IconButton(
            onClick = onDelete,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Usuń",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
