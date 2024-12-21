package com.example.todo.data

import com.example.todo.BuildConfig
import android.content.Context
import com.example.todo.models.HAEntity
import com.example.todo.models.websocket.HAWebSocketMessage
import com.example.todo.services.HAWebSocketService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HARepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val webSocketService: HAWebSocketService,
    private val haEntityDao: HAEntityDao
) {
    private val haServerUrl = BuildConfig.HA_SERVER_URL
    private val haToken = BuildConfig.HA_TOKEN

    val messages = webSocketService.messages.transform { message ->
        emit(message)
        when (message) {
            is HAWebSocketMessage.AuthResponse -> {
                if (message.success) {
                    webSocketService.subscribeToStateChanges()
                }
            }
            is HAWebSocketMessage.StateChanged -> {
                processStateChange(message)
            }
            else -> {}
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

    fun connectToHA() = webSocketService.connect(haServerUrl, haToken)
    fun disconnect() = webSocketService.disconnect()
}