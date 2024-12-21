package com.example.todo.di

import com.example.todo.services.HAWebSocketService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {
    @Provides
    @Singleton
    fun provideWebSocketService(): HAWebSocketService {
        return HAWebSocketService()
    }
}