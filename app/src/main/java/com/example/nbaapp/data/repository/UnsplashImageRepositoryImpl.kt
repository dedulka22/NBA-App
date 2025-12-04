package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.UnsplashApi
import com.example.nbaapp.domain.repository.ImageRepository

/**
 * Implementation of ImageRepository using Unsplash API.
 *
 * @param unsplashApi The Unsplash API interface
 */
class UnsplashImageRepositoryImpl(
    private val unsplashApi: UnsplashApi
) : ImageRepository {

    override suspend fun getImageUrl(query: String): String {
        return try {
            val response = unsplashApi.showImageNBA(
                page = 1,
                query = query
            )
            response.results.firstOrNull()?.urls?.small ?: DEFAULT_IMAGE_URL
        } catch (e: Exception) {
            DEFAULT_IMAGE_URL
        }
    }

    companion object {
        private const val DEFAULT_IMAGE_URL =
            "https://masterbundles.com/wp-content/uploads/2023/03/fsf-490.png"
    }
}
