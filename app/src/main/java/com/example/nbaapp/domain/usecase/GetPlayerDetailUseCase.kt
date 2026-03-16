package com.example.nbaapp.domain.usecase

import com.example.nbaapp.domain.model.PlayerDetail
import com.example.nbaapp.domain.repository.ImageRepository
import com.example.nbaapp.domain.repository.ImageRepository.Companion.DEFAULT_IMAGE_URL
import com.example.nbaapp.domain.repository.ImageRepository.Companion.IMAGE_FETCH_TIMEOUT_MS
import com.example.nbaapp.domain.repository.PlayerDetailRepository
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Use case for getting player details with image.
 * Combines player data from NBA API with image from Unsplash.
 *
 * @param playerDetailRepository The repository for player details
 * @param imageRepository The repository for fetching images
 */
class GetPlayerDetailUseCase(
    private val playerDetailRepository: PlayerDetailRepository,
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(playerId: Int): Pair<PlayerDetail, String> {
        val playerDetail = playerDetailRepository.getPlayerDetail(playerId)
        val imageUrl = withTimeoutOrNull(IMAGE_FETCH_TIMEOUT_MS) {
            imageRepository.getImageUrl("${playerDetail.firstName} ${playerDetail.lastName} basketball")
        } ?: DEFAULT_IMAGE_URL

        return Pair(playerDetail, imageUrl)
    }

}