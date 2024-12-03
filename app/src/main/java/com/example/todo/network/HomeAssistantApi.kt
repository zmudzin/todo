package com.example.todo.network

import com.example.todo.models.ShoppingItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface HomeAssistantApi {
    @GET("api/shopping_list")
    suspend fun getShoppingList(
        @Header("Authorization") authHeader: String
    ): Response<List<ShoppingItem>>
}
