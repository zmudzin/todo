package com.example.todo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Definicje nowoczesnych kolorów
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EA),       // Intensywny fiolet
    onPrimary = Color.White,
    secondary = Color(0xFF03DAC5),     // Turkusowy
    onSecondary = Color.Black,
    surface = Color.White,
    onSurface = Color(0xFF202124),     // Ciemny szary
    background = Color(0xFFF2F2F7),    // Jasny szary
    onBackground = Color(0xFF202124)   // Ciemny szary
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),       // Jasny fiolet
    onPrimary = Color.Black,
    secondary = Color(0xFF03DAC5),     // Turkusowy
    onSecondary = Color.Black,
    surface = Color(0xFF121212),       // Ciemny szary
    onSurface = Color(0xFFECECEC),     // Jasny szary
    background = Color(0xFF202124),    // Bardzo ciemny szary
    onBackground = Color(0xFFECECEC)   // Jasny szary
)

// Czcionki
private val AppTypography = Typography(
    titleLarge = Typography().titleLarge.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    bodyLarge = Typography().bodyLarge.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelLarge = Typography().labelLarge.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)

@Composable
fun ToDoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}
