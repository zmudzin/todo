package com.example.todo.services

import android.util.Log
import com.example.todo.models.websocket.HAWebSocketMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HAWebSocketService @Inject constructor() {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val _messageChannel = Channel<HAWebSocketMessage>(Channel.BUFFERED)
    val messages: Flow<HAWebSocketMessage> = _messageChannel.receiveAsFlow()

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
                        "auth_invalid" -> {
                            Log.e(TAG, "Autoryzacja nieudana")
                            runBlocking {
                                _messageChannel.send(HAWebSocketMessage.AuthResponse(
                                    id = 1,
                                    type = "auth_invalid",
                                    success = false
                                ))
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Błąd parsowania wiadomości", e)
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
        val subscribeMessage = """
        {
            "id": 18,
            "type": "subscribe_events",
            "event_type": "state_changed"
        }
    """.trimIndent()
        Log.d(TAG, "Wysyłam subskrypcję zdarzeń: $subscribeMessage")
        webSocket?.send(subscribeMessage)
    }

    fun disconnect() {
        webSocket?.close(1000, "Zamykanie połączenia")
        webSocket = null
    }

    companion object {
        private const val TAG = "HAWebSocketService"
    }
}