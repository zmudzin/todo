package com.example.todo.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectDragGestures

class DragController<T>(
    private val items: List<T>,
    private val onMove: (Int, Int) -> Unit
) {
    private var draggedItemIndex: Int? = null
    private var totalDragOffset = 0f

    fun startDrag(index: Int) {
        draggedItemIndex = index
        totalDragOffset = 0f
    }

    fun updateDrag(dragAmountY: Float, itemHeight: Float, taskCount: Int): Int? {
        val fromIndex = draggedItemIndex ?: return null

        totalDragOffset += dragAmountY

        val targetIndex = (fromIndex + (totalDragOffset / itemHeight).toInt())
            .coerceIn(0, taskCount - 1)

        if (targetIndex != fromIndex) {
            onMove(fromIndex, targetIndex)
            draggedItemIndex = targetIndex
            totalDragOffset -= (targetIndex - fromIndex) * itemHeight
        }

        return draggedItemIndex
    }

    fun endDrag() {
        draggedItemIndex = null
        totalDragOffset = 0f
    }

    fun dragModifier(index: Int, itemHeight: Float, taskCount: Int): Modifier = Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDragStart = { startDrag(index) },
            onDragEnd = { endDrag() },
            onDragCancel = { endDrag() },
            onDrag = { change, dragAmount ->
                change.consume()
                updateDrag(dragAmount.y, itemHeight, taskCount)
            }
        )
    }
}
