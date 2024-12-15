package com.example.todo.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

class DragController<T>(
    private val items: List<T>,
    private val onMove: (Int, Int) -> Unit
) {
    private var draggedItemIndex: Int? = null
    private var totalDragOffset = 0f

    /**
     * Funkcja rozpoczynająca przeciąganie.
     */
    fun startDrag(index: Int) {
        draggedItemIndex = index
        totalDragOffset = 0f
    }

    /**
     * Funkcja aktualizująca logikę przeciągania i przenoszenia elementu.
     */
    fun updateDrag(dragAmountY: Float, itemHeight: Float): Int? {
        val fromIndex = draggedItemIndex ?: return null

        // Aktualizuj całkowite przesunięcie w pionie
        totalDragOffset += dragAmountY

        // Oblicz docelowy indeks na podstawie przesunięcia
        val targetIndex = (fromIndex + (totalDragOffset / itemHeight).toInt())
            .coerceIn(0, items.size - 1) // Zapewnij zakres indeksów w ramach listy

        // Jeśli indeks docelowy się zmienił, przesuń element
        if (targetIndex != fromIndex) {
            onMove(fromIndex, targetIndex)
            draggedItemIndex = targetIndex
            totalDragOffset -= (targetIndex - fromIndex) * itemHeight // Zresetuj przesunięcie dla następnych obliczeń
        }

        return draggedItemIndex
    }

    /**
     * Funkcja kończąca przeciąganie.
     */
    fun endDrag() {
        draggedItemIndex = null
        totalDragOffset = 0f
    }

    /**
     * Modyfikator przeciągania dla elementów listy.
     */
    fun dragModifier(index: Int, itemHeight: Float): Modifier = Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDragStart = { startDrag(index) }, // Rozpocznij przeciąganie
            onDragEnd = { endDrag() },         // Zakończ przeciąganie
            onDragCancel = { endDrag() },      // Obsługa anulowania przeciągania
            onDrag = { change, dragAmount ->   // Obsługa przesunięcia
                change.consume()
                updateDrag(dragAmount.y, itemHeight)
            }
        )
    }
}
