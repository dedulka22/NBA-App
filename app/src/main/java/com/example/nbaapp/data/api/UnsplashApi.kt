package com.example.nbaapp.data.api

import com.example.nbaapp.BuildConfig
import com.example.nbaapp.data.model.UnsplashImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Unsplash API interface
 */
interface UnsplashApi {

    @GET("search/photos")
    suspend fun showImageNBA(
        @Query("page") page: Int,
        @Query("query") query: String,
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID
    ): UnsplashImageResponse
}