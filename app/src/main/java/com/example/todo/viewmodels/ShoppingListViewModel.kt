package com.example.todo.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.models.ShoppingItem
import com.example.todo.network.ApiClient
import kotlinx.coroutines.launch

class ShoppingListViewModel : ViewModel() {
    val shoppingItems = mutableListOf<ShoppingItem>()

    fun fetchShoppingList(token: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("FETCH_SHOPPING_LIST", "Wysyłam zapytanie do API")
                val response = ApiClient.api.getShoppingList("Bearer $token")

                if (response.isSuccessful) {
                    // Obsługa pustej odpowiedzi
                    if (response.body() == null || response.body()?.isEmpty() == true) {
                        Log.e("FETCH_SHOPPING_LIST", "Odpowiedź jest pusta lub null")
                        onError("Pusta odpowiedź z serwera")
                        return@launch
                    }

                    // Przetwarzanie poprawnej odpowiedzi
                    shoppingItems.clear()
                    response.body()?.let {
                        shoppingItems.addAll(it)
                    }
                    Log.d("FETCH_SHOPPING_LIST", "Lista zakupów pobrana pomyślnie: ${shoppingItems.size} pozycji")
                    onSuccess()
                } else {
                    // Obsługa błędów serwera (np. 4xx, 5xx)
                    val errorBody = response.errorBody()?.string()
                    Log.e("FETCH_SHOPPING_LIST", "Błąd API: ${response.code()} - ${response.message()} - $errorBody")
                    onError("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                // Obsługa wyjątków
                Log.e("FETCH_SHOPPING_LIST", "Wystąpił wyjątek: ${e.localizedMessage}", e)
                onError("Exception: ${e.localizedMessage}")
            }
        }
    }



}
