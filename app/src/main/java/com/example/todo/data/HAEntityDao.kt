package com.example.todo.data

import androidx.room.*
import com.example.todo.models.HAEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HAEntityDao {
    @Query("SELECT * FROM ha_entities ORDER BY entityId ASC")
    fun getAllEntities(): Flow<List<HAEntity>>

    @Query("SELECT * FROM ha_entities WHERE domain = :domain")
    fun getEntitiesByDomain(domain: String): Flow<List<HAEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntity(entity: HAEntity)

    @Query("DELETE FROM ha_entities WHERE entityId = :entityId")
    suspend fun deleteEntity(entityId: String)
}