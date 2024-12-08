package com.example.todo.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Definicja schematu kolorów z Figma
private val LightColors = lightColorScheme(
    primary = Color(0xFF6200EE),          // Fioletowy nagłówek
    onPrimary = Color.White,             // Biały tekst na fioletowym tle
    background = Color(0xFFFFFFFF),      // Białe tło aplikacji
    onBackground = Color.Black,          // Czarny tekst na białym tle
    primaryContainer = Color(0xFF03DAC5),// Turkusowy przycisk "Dodaj"
    onPrimaryContainer = Color.White     // Biały symbol w turkusowym przycisku
)

// Definicja typografii z Figma (możesz dostosować dalej, jeśli masz szczegółowe informacje)
private val AppTypography = Typography(
    titleLarge = Typography().titleLarge.copy(
        color = Color.White // Biały tekst dla nagłówków
    ),
    bodyLarge = Typography().bodyLarge.copy(
        color = Color.Black // Czarny tekst dla treści zadań
    )
)

// Funkcja motywu aplikacji
@Composable
fun ToDoAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}
