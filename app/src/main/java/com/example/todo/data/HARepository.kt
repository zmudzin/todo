package com.example.todo.data

import com.example.todo.BuildConfig
import android.content.Context
import android.util.Log
import com.example.todo.models.HAEntity
import kotlinx.coroutines.flow.onEach
import com.example.todo.models.events.TodoEvent
import com.example.todo.models.websocket.HAWebSocketMessage
import com.example.todo.services.HAWebSocketService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HARepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val webSocketService: HAWebSocketService,
    private val haEntityDao: HAEntityDao,
    private val taskDao: TaskDao
) {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            webSocketService.todoEvents.collect { event ->
                when (event) {
                    is TodoEvent.ItemsUpdated -> taskDao.insertAll(event.items)
                    is TodoEvent.Error -> Log.e("HARepository", "Error: ${event.exception.message}")
                }
            }
        }
    }

    val messages = webSocketService.messages.transform { message ->
        emit(message)
        when (message) {
            is HAWebSocketMessage.AuthResponse -> {
                if (message.success) webSocketService.subscribeToStateChanges()
            }
            is HAWebSocketMessage.StateChanged -> processStateChange(message)
            else -> {}
        }
    }

    val todoUpdates = webSocketService.todoEvents.onEach { event ->
        when (event) {
            is TodoEvent.ItemsUpdated -> taskDao.insertAll(event.items)
            is TodoEvent.Error -> Log.e("HARepository", "Error: ${event.exception.message}")
        }
    }

    private suspend fun processStateChange(message: HAWebSocketMessage.StateChanged) {
        val entity = HAEntity(
            entityId = message.entityId,
            state = message.state,
            lastUpdated = message.lastUpdated,
            attributes = message.attributes.toString(),
            domain = message.entityId.split(".").firstOrNull() ?: return,
            friendlyName = message.attributes.optString("friendly_name")
        )
        haEntityDao.insertEntity(entity)
    }

    fun connectToHA() = webSocketService.connect(BuildConfig.HA_SERVER_URL, BuildConfig.HA_TOKEN)
    fun disconnect() = webSocketService.disconnect()
}