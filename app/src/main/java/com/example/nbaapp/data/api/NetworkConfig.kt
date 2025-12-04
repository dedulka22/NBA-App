package com.example.nbaapp.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Provides OkHttpClient for NBA API with authentication header.
 *
 * @param apiKey The API key for NBA API
 * @param isDebug Whether to enable logging
 */
fun provideNBAOkHttpClient(apiKey: String, isDebug: Boolean): OkHttpClient {
    val builder = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", apiKey)
                .build()
            chain.proceed(newRequest)
        }

    // Add logging only in debug builds
    if (isDebug) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        builder.addInterceptor(loggingInterceptor)
    }

    return builder.build()
}

/**
 * Provides OkHttpClient for Unsplash API with client_id query parameter.
 *
 * @param clientId The client ID for Unsplash API
 * @param isDebug Whether to enable logging
 */
fun provideUnsplashOkHttpClient(clientId: String, isDebug: Boolean): OkHttpClient {
    val builder = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val url = originalRequest.url.newBuilder()
                .addQueryParameter("client_id", clientId)
                .build()
            val newRequest = originalRequest.newBuilder()
                .url(url)
                .build()
            chain.proceed(newRequest)
        }

    // Add logging only in debug builds
    if (isDebug) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        builder.addInterceptor(loggingInterceptor)
    }

    return builder.build()
}

/**
 * Provides Retrofit instance for NBA API.
 */
fun provideNBARetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.balldontlie.io/v1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

/**
 * Provides Retrofit instance for Unsplash API.
 */
fun provideUnsplashRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.unsplash.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

/**
 * Provides NBA API service.
 */
fun provideNBAApi(retrofit: Retrofit): NBAApi {
    return retrofit.create(NBAApi::class.java)
}

/**
 * Provides Unsplash API service.
 */
fun provideUnsplashApi(retrofit: Retrofit): UnsplashApi {
    return retrofit.create(UnsplashApi::class.java)
}
