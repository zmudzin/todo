package com.example.todo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Header(title: String, onRefresh: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary) // Tło nagłówka
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tytuł w środku
        Text(
            text = title,
            modifier = Modifier
                .weight(1f), // Wyśrodkowanie dzięki użyciu weight
            style = MaterialTheme.typography.titleLarge, // Zastosowanie typografii z motywu
            color = MaterialTheme.colorScheme.onPrimary, // Kolor tekstu z motywu
            textAlign = TextAlign.Center
        )

        // Ikona odświeżania po prawej
        IconButton(onClick = onRefresh) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Odśwież",
                tint = MaterialTheme.colorScheme.onPrimary // Kolor ikony z motywu
            )
        }
    }
}
