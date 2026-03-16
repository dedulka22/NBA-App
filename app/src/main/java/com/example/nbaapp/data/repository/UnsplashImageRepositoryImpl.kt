package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.UnsplashApi
import com.example.nbaapp.domain.repository.ImageRepository
import com.example.nbaapp.domain.repository.ImageRepository.Companion.DEFAULT_IMAGE_URL
import com.example.nbaapp.domain.util.ConnectivityObserver
import retrofit2.HttpException
import java.io.IOException

/**
 * Implementation of ImageRepository using Unsplash API.
 *
 * @param unsplashApi The Unsplash API interface
 */
class UnsplashImageRepositoryImpl(
    private val unsplashApi: UnsplashApi,
    private val connectivityObserver: ConnectivityObserver
) : ImageRepository {

    override suspend fun getImageUrl(query: String): String {
        if (!connectivityObserver.isCurrentlyConnected) return DEFAULT_IMAGE_URL
        return try {
            val response = unsplashApi.showImageNBA(
                page = FIRST_PAGE,
                query = query
            )
            response.results.firstOrNull()?.urls?.small ?: DEFAULT_IMAGE_URL
        } catch (e: IOException) {
            DEFAULT_IMAGE_URL
        } catch (e: HttpException) {
            DEFAULT_IMAGE_URL
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}
