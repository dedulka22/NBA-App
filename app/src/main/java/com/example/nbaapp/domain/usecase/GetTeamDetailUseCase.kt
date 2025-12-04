package com.example.nbaapp.domain.usecase

import com.example.nbaapp.domain.repository.ImageRepository
import com.example.nbaapp.domain.repository.TeamDetailRepository
import com.example.nbaapp.ui.model.TeamUiModel

/**
 * Use case for getting team details with image.
 * Combines team data from NBA API with image from Unsplash.
 *
 * @param teamDetailRepository The repository for team details
 * @param imageRepository The repository for fetching images
 */
class GetTeamDetailUseCase(
    private val teamDetailRepository: TeamDetailRepository,
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(teamId: Int): TeamUiModel {
        val team = teamDetailRepository.getTeamDetail(teamId)
        val imageUrl = imageRepository.getImageUrl("${team.fullName} NBA team")

        return TeamUiModel(
            team = team,
            imageUrl = imageUrl
        )
    }
}