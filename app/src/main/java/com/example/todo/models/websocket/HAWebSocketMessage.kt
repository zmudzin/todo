package com.example.todo.models.websocket

sealed interface HAWebSocketMessage {
    val type: String
    val id: Int

    data class AuthMessage(
        override val id: Int = 1,
        override val type: String = "auth",
        val accessToken: String
    ) : HAWebSocketMessage

    data class AuthResponse(
        override val id: Int,
        override val type: String,
        val success: Boolean,
        val haVersion: String? = null
    ) : HAWebSocketMessage

    data class StateChanged(
        override val id: Int,
        override val type: String = "event",
        val entityId: String,
        val state: String,
        val lastUpdated: String,
        val attributes: org.json.JSONObject
    ) : HAWebSocketMessage

    data class TodoStateChanged(
        override val id: Int,
        override val type: String = "event",
        val entityId: String,
        val state: String,
        val lastUpdated: String,
        val attributes: org.json.JSONObject,
        val items: List<TodoItem>
    ) : HAWebSocketMessage

    data class TodoItem(
        val name: String,
        val complete: Boolean
    )
}