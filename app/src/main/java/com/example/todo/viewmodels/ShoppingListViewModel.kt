package com.example.todo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.models.CompleteItemRequest
import com.example.todo.models.ShoppingItem
import com.example.todo.network.ApiClient
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateListOf

class ShoppingListViewModel : ViewModel() {
    val shoppingItems = mutableStateListOf<ShoppingItem>()

    fun fetchShoppingList(token: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ApiClient.api.getShoppingList("Bearer $token")
                if (response.isSuccessful) {
                    val items = response.body() ?: emptyList()
                    shoppingItems.clear()
                    shoppingItems.addAll(items)
                    onSuccess()
                } else {
                    onError("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Exception: ${e.localizedMessage}")
            }
        }
    }

    fun toggleItemState(
        token: String,
        item: ShoppingItem,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val service = if (item.complete) "incomplete_item" else "complete_item"

                val response = ApiClient.api.sendAction(
                    authHeader = "Bearer $token",
                    service = service,
                    data = CompleteItemRequest(name = item.name)
                )

                if (response.isSuccessful) {
                    val index = shoppingItems.indexOfFirst { it.id == item.id }
                    if (index >= 0) {
                        // Wymuś aktualizację UI
                        shoppingItems[index] = item.copy(complete = !item.complete)
                    }
                    onSuccess()
                } else {
                    onError("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Exception: ${e.localizedMessage}")
            }
        }
    }
}



