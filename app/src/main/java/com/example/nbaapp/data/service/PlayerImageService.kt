package com.example.nbaapp.data.service

import com.example.nbaapp.data.api.UnsplashApi

/**
 * Service for fetching player images
 * @param unsplashApi The Unsplash API
 */
class PlayerImageService(
    private val unsplashApi: UnsplashApi
) {

    suspend fun getNBAImage(playerId: Int): String {
        return try {
            val urlImage = unsplashApi.showImageNBA(
                page = playerId,
                query = QUERY_NBA_PLAYER
            )

            val index = playerId - 1
            if (index in urlImage.results.indices) {
                urlImage.results[index].urls.small
            } else {
                urlImage.results.firstOrNull()?.urls?.small ?: DEFAULT_IMAGE_URL
            }
        } catch (e: Exception) {
            println("Error fetching player image: ${e.message}")
            DEFAULT_IMAGE_URL
        }
    }

    companion object {
        private const val DEFAULT_IMAGE_URL =
            "https://masterbundles.com/wp-content/uploads/2023/03/fsf-490.png"
        private const val QUERY_NBA_PLAYER = "basketball player"
    }
}
