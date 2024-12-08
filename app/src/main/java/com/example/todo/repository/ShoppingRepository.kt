package com.example.todo.repository

import com.example.todo.models.AddItemRequest
import com.example.todo.models.CompleteItemRequest
import com.example.todo.models.ShoppingItem
import com.example.todo.network.HomeAssistantApi
import retrofit2.Response

class ShoppingRepository(private val api: HomeAssistantApi) {

    suspend fun fetchShoppingList(token: String): Response<List<ShoppingItem>> {
        return api.getShoppingList("Bearer $token")
    }

    suspend fun toggleItemState(token: String, service: String, itemName: String): Response<Unit> {
        return api.sendAction(
            authHeader = "Bearer $token",
            service = service,
            data = CompleteItemRequest(name = itemName)
        )
    }

    suspend fun addItem(token: String, itemName: String): Response<Unit> {
        return api.addItem(
            authHeader = "Bearer $token",
            data = AddItemRequest(name = itemName)
        )
    }

    suspend fun removeItem(token: String, itemName: String): Response<Unit> {
        return api.removeItem(
            authHeader = "Bearer $token",
            data = CompleteItemRequest(name = itemName)
        )
    }
    suspend fun editItemOnServer(token: String, oldName: String, newName: String): Boolean {
        // Usuń istniejące zadanie
        val removeResponse = removeItem(token, oldName)
        if (!removeResponse.isSuccessful) {
            return false // Jeśli usuwanie nie powiodło się, zwróć błąd
        }    // Dodaj nowe zadanie
        val addResponse = addItem(token, newName)
        return addResponse.isSuccessful
    }
}
