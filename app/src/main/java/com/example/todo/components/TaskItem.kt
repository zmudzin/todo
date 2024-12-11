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
            .padding(horizontal = 16.dp, vertical = 2.dp) // Mniejsze odstępy zewnętrzne
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp)) // Mniejszy zaokrąglony kształt
            .padding(horizontal = 8.dp, vertical = 4.dp) // Mniejszy padding wewnętrzny
            .clickable { onEdit(task.name) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox po lewej stronie
        Checkbox(
            checked = task.complete,
            onCheckedChange = onTaskCheckedChange,
            modifier = Modifier.padding(end = 4.dp), // Mniejszy odstęp
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary, // Kolor zaznaczonego checkboxa
                uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Kolor niezaznaczonego
                checkmarkColor = MaterialTheme.colorScheme.onPrimary // Kolor znaku zaznaczenia
            )
        )

        // Tekst zadania
        Text(
            text = task.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall, // Użycie mniejszej typografii
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface // Kolor tekstu
        )

        // Ikona kosza po prawej stronie
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(20.dp) // Zmniejszony rozmiar ikony
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Usuń",
                tint = MaterialTheme.colorScheme.onBackground // Kolor ikony kosza
            )
        }
    }
}


