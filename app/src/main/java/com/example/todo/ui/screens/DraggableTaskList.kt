package com.example.todo.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.todo.components.DragController
import com.example.todo.models.Task
import com.example.todo.components.TaskItem
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableTaskList(
    tasks: List<Task>,
    onMove: (Int, Int) -> Unit,
    onTaskCheckedChange: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit,
    onTaskEdit: (Task) -> Unit
) {
    val dragController = remember { DragController(tasks, onMove) }
    val itemHeight = remember { mutableStateOf(0f) }
    val isDragging = remember { mutableStateOf(false) }
    val draggedItemIndex = remember { mutableStateOf<Int?>(null) }

    LazyColumn {
        itemsIndexed(
            items = tasks,
            key = { _, task -> task.id }
        ) { index, task ->
            val offsetY = animateFloatAsState(
                targetValue = if (isDragging.value && draggedItemIndex.value == index)
                    dragController.dragAmount else 0f
            )

            AnimatedVisibility(
                visible = true,
                exit = slideOutHorizontally(
                    targetOffsetX = { it * 2 },
                    animationSpec = tween(durationMillis = 350)
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 350)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.DragHandle,
                        contentDescription = "Przeciągnij",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                            .graphicsLayer {
                                alpha = if (isDragging.value && draggedItemIndex.value == index) 0.5f else 1f
                                scaleX = if (isDragging.value && draggedItemIndex.value == index) 1.05f else 1f
                                scaleY = if (isDragging.value && draggedItemIndex.value == index) 1.05f else 1f
                            }
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = {
                                        isDragging.value = true
                                        draggedItemIndex.value = index
                                        dragController.startDrag(index)
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        dragController.updateDrag(dragAmount.y, itemHeight.value, tasks.size)
                                    },
                                    onDragEnd = {
                                        isDragging.value = false
                                        draggedItemIndex.value = null
                                        dragController.endDrag()
                                    },
                                    onDragCancel = {
                                        isDragging.value = false
                                        draggedItemIndex.value = null
                                        dragController.endDrag()
                                    }
                                )
                            }
                    )

                    TaskItem(
                        task = task,
                        onTaskCheckedChange = { isChecked -> onTaskCheckedChange(index, isChecked) },
                        onDelete = { onDelete(index) },
                        onEdit = { onTaskEdit(task) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset { IntOffset(0, offsetY.value.roundToInt()) }
                            .animateItem()
                            .onGloballyPositioned { coordinates ->
                                if (itemHeight.value == 0f) {
                                    itemHeight.value = coordinates.size.height.toFloat()
                                }
                            }
                    )
                }
            }
        }
    }
}