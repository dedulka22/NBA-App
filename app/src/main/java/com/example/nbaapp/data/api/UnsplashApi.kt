package com.example.nbaapp.data.api

import com.example.nbaapp.data.model.UnsplashImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Unsplash API interface.
 * The client_id is added via interceptor, not as a default parameter.
 */
interface UnsplashApi {

    @GET("search/photos")
    suspend fun showImageNBA(
        @Query("page") page: Int,
        @Query("query") query: String
    ): UnsplashImageResponse
}