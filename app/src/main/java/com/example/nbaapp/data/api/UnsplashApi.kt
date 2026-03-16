package com.example.nbaapp.data.api

import com.example.nbaapp.data.model.UnsplashImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Unsplash API interface.
 * The client_id is added via interceptor, not as a default parameter.
 */
interface UnsplashApi {

    @GET(ENDPOINT_SEARCH_PHOTOS)
    suspend fun showImageNBA(
        @Query(PARAM_PAGE) page: Int,
        @Query(PARAM_QUERY) query: String
    ): UnsplashImageResponse

    companion object {
        const val ENDPOINT_SEARCH_PHOTOS = "search/photos"
        const val PARAM_PAGE = "page"
        const val PARAM_QUERY = "query"
    }
}