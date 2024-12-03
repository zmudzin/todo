package com.example.todo.network

import com.example.todo.models.AddItemRequest
import com.example.todo.models.CompleteItemRequest
import com.example.todo.models.ShoppingItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeAssistantApi {
    @GET("api/shopping_list")
    suspend fun getShoppingList(
        @Header("Authorization") authHeader: String
    ): Response<List<ShoppingItem>>

    @POST("api/services/{domain}/{service}")
    suspend fun sendAction(
        @Header("Authorization") authHeader: String,
        @Path("domain") domain: String = "shopping_list", // Domyślny domena
        @Path("service") service: String, // Usługa np. complete_item lub incomplete_item
        @Body data: CompleteItemRequest
    ): Response<Unit>
    @POST("api/services/shopping_list/add_item")
    suspend fun addItem(
        @Header("Authorization") authHeader: String,
        @Body data: AddItemRequest
    ): Response<Unit>

}
