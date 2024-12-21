package com.example.todo.services

import android.util.Log
import com.example.todo.models.Task
import com.example.todo.models.events.TodoEvent
import com.example.todo.models.websocket.HAWebSocketMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HAWebSocketService @Inject constructor() {
    private var currentMessageId = 1
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val _messageChannel = Channel<HAWebSocketMessage>(Channel.BUFFERED)
    val messages: Flow<HAWebSocketMessage> = _messageChannel.receiveAsFlow()

    private val _todoEvents = MutableSharedFlow<TodoEvent>()
    val todoEvents = _todoEvents.asSharedFlow()

    fun connect(serverUrl: String, token: String) {

        Log.d(TAG, "Próba połączenia z: $serverUrl")

        val request = Request.Builder()
            .url(serverUrl)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "WebSocket połączony, czekam na auth_required")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Otrzymano wiadomość: $text")
                try {
                    val jsonObject = JSONObject(text)
                    val type = jsonObject.getString("type")
                    Log.d(TAG, "Typ wiadomości: $type")

                    when (type) {
                        "auth_required" -> {
                            Log.d(TAG, "Otrzymano auth_required, wysyłam autoryzację")
                            sendAuthMessage(token)
                        }
                        "auth_ok" -> {
                            Log.d(TAG, "Autoryzacja udana")
                            runBlocking {
                                _messageChannel.send(HAWebSocketMessage.AuthResponse(
                                    id = 1,
                                    type = "auth_ok",
                                    success = true,
                                    haVersion = jsonObject.optString("ha_version")
                                ))
                            }
                        }
                        "auth_invalid" -> { /* istniejący kod */ }
                        "event" -> {
                            Log.d(TAG, "Otrzymano event: ${jsonObject.toString(2)}")
                            handleEventMessage(jsonObject)
                        }
                        "result" -> {
                            Log.d(TAG, "Otrzymano result: ${jsonObject.toString(2)}")
                            handleResultMessage(jsonObject)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Błąd parsowania wiadomości", e)
                    e.printStackTrace()
                }
            }

            private fun handleEventMessage(jsonObject: JSONObject) {
                val event = jsonObject.getJSONObject("event")
                val data = event.getJSONObject("data")
                val entityId = data.getString("entity_id")

                if (entityId.startsWith("todo.")) {
                    getTodoItems(entityId)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "Błąd WebSocket: ${t.message}")
                t.printStackTrace()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket zamknięty: $reason")
            }
        })
    }

    private fun handleResultMessage(jsonObject: JSONObject) {
        if (jsonObject.optBoolean("success", false)) {
            val result = jsonObject.optJSONObject("result")
            if (result?.has("items") == true) {
                val items = parseTodoItems(result)
                runBlocking {
                    _todoEvents.emit(TodoEvent.ItemsUpdated(items.map {
                        Task(
                            name = it.name,
                            isChecked = it.complete,
                            position = 0 // domyślna pozycja
                        )
                    }))
                }
            }
        } else {
            val error = jsonObject.optJSONObject("error")
            runBlocking {
                _todoEvents.emit(TodoEvent.Error(Exception(error?.optString("message"))))
            }
        }
    }
    private fun sendAuthMessage(token: String) {
        val authMessage = """
            {
                "type": "auth",
                "access_token": "$token"
            }
        """.trimIndent()
        Log.d(TAG, "Wysyłam wiadomość autoryzacyjną: $authMessage")
        webSocket?.send(authMessage)
    }

    fun subscribeToStateChanges() {
        val messageId = getNextMessageId()
        val subscribeMessage = """
        {
            "id": $messageId,
            "type": "subscribe_events",
            "event_type": "state_changed"
        }
        """.trimIndent()
        Log.d(TAG, "Wysyłam subskrypcję zdarzeń: $subscribeMessage")
        webSocket?.send(subscribeMessage)
    }

    private fun parseTodoItems(attributes: JSONObject): List<HAWebSocketMessage.TodoItem> {
        return try {
            val items = attributes.optJSONArray("items") ?: return emptyList()
            List(items.length()) { index ->
                val item = items.getJSONObject(index)
                HAWebSocketMessage.TodoItem(
                    name = item.getString("name"),
                    complete = item.getBoolean("complete")
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Błąd parsowania zadań", e)
            emptyList()
        }
    }
    fun getNextMessageId(): Int {
        synchronized(this) {
            return currentMessageId++
        }
    }

    fun disconnect() {
        webSocket?.close(1000, "Zamykanie połączenia")
        webSocket = null
    }

    companion object {
        private const val TAG = "HAWebSocketService"
    }
    private fun getTodoItems(entityId: String) {
        val messageId = getNextMessageId()
        val message = """
        {
            "id": $messageId,
            "type": "call_service",
            "domain": "todo",
            "service": "get_items",
            "target": {
                "entity_id": "$entityId"
            },
            "return_response": true
        }
        """.trimIndent()
        webSocket?.send(message)
    }
}
