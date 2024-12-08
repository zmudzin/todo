package com.example.todo.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.models.ShoppingItem
import com.example.todo.repository.ShoppingRepository
import kotlinx.coroutines.launch

class ShoppingListViewModel(private val repository: ShoppingRepository) : ViewModel() {
    val shoppingItems = mutableStateListOf<ShoppingItem>()

    fun fetchShoppingList(token: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.fetchShoppingList(token)
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
                val response = repository.toggleItemState(token, service, item.name)

                if (response.isSuccessful) {
                    val index = shoppingItems.indexOfFirst { it.id == item.id }
                    if (index >= 0) {
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

    fun removeItem(
        token: String,
        itemName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.removeItem(token, itemName)

                if (response.isSuccessful) {
                    val index = shoppingItems.indexOfFirst { it.name == itemName }
                    if (index >= 0) {
                        shoppingItems.removeAt(index)
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
    fun editItem(
        token: String,
        oldName: String,
        newName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val success = repository.editItemOnServer(token, oldName, newName)
                if (success) {
                    val index = shoppingItems.indexOfFirst { it.name == oldName }
                    if (index >= 0) {
                        shoppingItems[index] = shoppingItems[index].copy(name = newName)
                    }
                    onSuccess()
                } else {
                    onError("Edycja nie powiodła się")
                }
            } catch (e: Exception) {
                onError("Exception: ${e.localizedMessage}")
            }
        }
    }
    fun addItem(
        token: String,
        itemName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.addItem(token, itemName)
                if (response.isSuccessful) {
                    fetchShoppingList(token, onSuccess, onError)
                } else {
                    onError("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Exception: ${e.localizedMessage}")
            }
        }
    }


}
