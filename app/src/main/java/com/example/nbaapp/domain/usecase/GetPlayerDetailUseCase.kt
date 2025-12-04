package com.example.nbaapp.domain.usecase

import com.example.nbaapp.domain.repository.ImageRepository
import com.example.nbaapp.domain.repository.PlayerDetailRepository
import com.example.nbaapp.ui.model.PlayerDetailUiModel

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
    suspend operator fun invoke(playerId: Int): PlayerDetailUiModel {
        val playerDetail = playerDetailRepository.getPlayerDetail(playerId)
        val imageUrl =
            imageRepository.getImageUrl("${playerDetail.firstName} ${playerDetail.lastName} basketball")

        return PlayerDetailUiModel(
            playerDetail = playerDetail,
            imageUrl = imageUrl
        )
    }
}