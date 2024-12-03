package com.example.todo.network

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://ha.losia3.xyz/"

    val api: HomeAssistantApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS) // Timeout na połączenie
                    .readTimeout(30, TimeUnit.SECONDS)    // Timeout na odpowiedź
                    .writeTimeout(30, TimeUnit.SECONDS)   // Timeout na zapis
                    .addInterceptor { chain ->
                        val request = chain.request()
                        // Użycie metody url() zamiast bezpośredniego odwołania do request.url
                        Log.d("HTTP_REQUEST", "Wysyłam zapytanie: ${request.url()}")
                        chain.proceed(request)
                    }
                    .build()
            )
            .build()
            .create(HomeAssistantApi::class.java)
    }
}
