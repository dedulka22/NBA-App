package com.example.nbaapp.data.api

import com.example.nbaapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * API client object
 * Contains the NBA API and Unsplash API
 * Uses Retrofit to create the API interfaces
 * Uses OkHttp to create the client
 * Uses GsonConverterFactory to convert JSON to Kotlin objects
 * Uses HttpLoggingInterceptor to log network requests
 * Uses BuildConfig to get the API key
 */
object ApiClient {
    private const val BASE_URL = "https://api.balldontlie.io/v1/"
    private const val BASE_URL_UNSPLASH = "https://api.unsplash.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val originalRequest: Request = chain.request()
            val newRequest: Request = originalRequest.newBuilder()
                .addHeader("Authorization", BuildConfig.API_KEY)
                .build()
            chain.proceed(newRequest)
        }
        .build()

    val nbaApi: NBAApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NBAApi::class.java)
    }

    private val unsplashClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    val unsplashApi : UnsplashApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_UNSPLASH)
            .client(unsplashClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashApi::class.java)
    }
}