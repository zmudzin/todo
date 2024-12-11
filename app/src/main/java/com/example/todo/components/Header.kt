package com.example.todo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Header(onSearch: (String) -> Unit, onRefresh: () -> Unit) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("") ) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background) // Użycie koloru secondary jako tło
            .padding(horizontal = 16.dp, vertical = 8.dp), // Padding dla odpowiedniego wyglądu
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Wyszukiwarka
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearch(it.text)
            },
            placeholder = {
                Text(
                    text = "Wyszukaj",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                //.height(52.dp) // Zmniejszona wysokość pola wyszukiwania
                .background(
                    color = Color.Black, // Wnętrze pola wyszukiwania
                    shape = RoundedCornerShape(36.dp) // Zaokrąglone rogi
                )
                .padding(horizontal = 12.dp, vertical = 1.dp), // Padding wewnętrzny
                colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = Color(0xFFF2F2F7),
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.width(8.dp))
    }
}