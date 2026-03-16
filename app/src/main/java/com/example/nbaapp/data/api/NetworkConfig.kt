package com.example.nbaapp.data.api

import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

private const val HTTP_CONNECT_TIMEOUT_SECONDS = 10L
private const val HTTP_READ_TIMEOUT_SECONDS = 60L
private const val HTTP_CALL_TIMEOUT_SECONDS = 150L
private const val HEADER_AUTHORIZATION = "Authorization"
private const val PARAM_CLIENT_ID = "client_id"
private const val NBA_API_BASE_URL = "https://api.balldontlie.io/v1/"
private const val UNSPLASH_API_BASE_URL = "https://api.unsplash.com/"
private const val HTTP_STATUS_TOO_MANY_REQUESTS = 429
private const val HEADER_RETRY_AFTER = "retry-after"
private const val DEFAULT_RETRY_AFTER_SECONDS = 30L
private const val MAX_RETRY_AFTER_SECONDS = 60L
private const val MAX_RETRIES = 2
private const val HTTP_CACHE_SIZE = 10L * 1024 * 1024 // 10 MB
private const val HTTP_CACHE_DIR_NAME = "http_cache"

fun provideNBAOkHttpClient(cacheDir: File, apiKey: String, isDebug: Boolean): OkHttpClient {
    val cache = Cache(File(cacheDir, HTTP_CACHE_DIR_NAME), HTTP_CACHE_SIZE)
    val builder = OkHttpClient.Builder()
        .cache(cache)
        .connectTimeout(HTTP_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(HTTP_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .callTimeout(HTTP_CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .addHeader(HEADER_AUTHORIZATION, apiKey)
                .build()
            var response = chain.proceed(newRequest)

            var retryCount = 0
            while (response.code == HTTP_STATUS_TOO_MANY_REQUESTS && retryCount < MAX_RETRIES) {
                val retryAfterSeconds = response.header(HEADER_RETRY_AFTER)
                    ?.toLongOrNull()?.coerceIn(1, MAX_RETRY_AFTER_SECONDS)
                    ?: DEFAULT_RETRY_AFTER_SECONDS
                response.close()
                Thread.sleep(retryAfterSeconds * 1000)
                response = chain.proceed(newRequest)
                retryCount++
            }

            response
        }

    if (isDebug) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        builder.addInterceptor(loggingInterceptor)
    }

    return builder.build()
}

fun provideUnsplashOkHttpClient(clientId: String, isDebug: Boolean): OkHttpClient {
    val builder = OkHttpClient.Builder()
        .connectTimeout(HTTP_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(HTTP_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val url = originalRequest.url.newBuilder()
                .addQueryParameter(PARAM_CLIENT_ID, clientId)
                .build()
            val newRequest = originalRequest.newBuilder()
                .url(url)
                .build()
            chain.proceed(newRequest)
        }

    if (isDebug) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        builder.addInterceptor(loggingInterceptor)
    }

    return builder.build()
}

fun provideNBARetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(NBA_API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideUnsplashRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(UNSPLASH_API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideNBAApi(retrofit: Retrofit): NBAApi {
    return retrofit.create(NBAApi::class.java)
}

fun provideUnsplashApi(retrofit: Retrofit): UnsplashApi {
    return retrofit.create(UnsplashApi::class.java)
}
