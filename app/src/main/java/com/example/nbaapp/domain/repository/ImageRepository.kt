package com.example.nbaapp.domain.repository

/**
 * Repository interface for fetching images.
 * This abstracts the image fetching logic from the data source implementation.
 */
interface ImageRepository {
    /**
     * Fetches an image URL based on a search query.
     *
     * @param query The search query for the image
     * @return The URL of the image, or a default image URL if not found
     */
    suspend fun getImageUrl(query: String): String
}
