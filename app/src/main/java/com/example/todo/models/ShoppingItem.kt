package com.example.todo.models

data class ShoppingItem(
    val name: String,
    val id: String,
    var complete: Boolean // Zmieniono na `var` dla łatwiejszej obsługi checkboxa
)
