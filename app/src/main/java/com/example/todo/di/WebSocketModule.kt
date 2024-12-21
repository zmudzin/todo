package com.example.todo.di

import android.content.Context
import com.example.todo.data.HAEntityDao

import com.example.todo.data.HARepository
import com.example.todo.services.HAWebSocketService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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