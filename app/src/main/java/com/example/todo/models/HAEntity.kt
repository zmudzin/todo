package com.example.todo.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ha_entities")
data class HAEntity(
    @PrimaryKey
    val entityId: String,
    val state: String,
    val lastUpdated: String,
    val attributes: String,
    val domain: String,
    val friendlyName: String?
)