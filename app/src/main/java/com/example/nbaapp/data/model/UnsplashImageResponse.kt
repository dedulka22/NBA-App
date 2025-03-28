package com.example.nbaapp.data.model

/**
 * Data class for Unsplash image response
 * @param results The list of Unsplash image DTOs
 */
data class UnsplashImageResponse (
    val results: List<UnsplashImageDto>
)

/**
 * Data class for Unsplash image DTO
 * @param id The image ID
 * @param urls The image URLs DTO
 */
data class UnsplashImageDto(
    val id: String,
    val urls: UnsplashImageUrlsDto
)

/**
 * Data class for Unsplash image URLs DTO
 * @param small The small image URL
 */
data class UnsplashImageUrlsDto(
    val small: String
)
