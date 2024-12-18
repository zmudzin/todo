package com.example.todo.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectDragGestures

class DragController<T>(
    private val items: List<T>,
    private val onMove: (Int, Int) -> Unit
) {
    private var draggedItemIndex: Int? = null
    var dragAmount = 0f
        private set

    fun startDrag(index: Int) {
        draggedItemIndex = index
        dragAmount = 0f
    }

    fun updateDrag(dragAmountY: Float, itemHeight: Float, taskCount: Int): Int? {
        dragAmount += dragAmountY
        val fromIndex = draggedItemIndex ?: return null

        val targetIndex = (fromIndex + (dragAmount / itemHeight).toInt())
            .coerceIn(0, taskCount - 1)

        if (targetIndex != fromIndex) {
            onMove(fromIndex, targetIndex)
            draggedItemIndex = targetIndex
            dragAmount -= (targetIndex - fromIndex) * itemHeight
        }

        return draggedItemIndex
    }

    fun endDrag() {
        draggedItemIndex = null
        dragAmount = 0f
    }
}