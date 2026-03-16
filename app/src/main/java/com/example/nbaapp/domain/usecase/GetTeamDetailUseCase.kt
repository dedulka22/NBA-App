package com.example.nbaapp.domain.usecase

import com.example.nbaapp.domain.model.Team
import com.example.nbaapp.domain.repository.ImageRepository
import com.example.nbaapp.domain.repository.ImageRepository.Companion.DEFAULT_IMAGE_URL
import com.example.nbaapp.domain.repository.ImageRepository.Companion.IMAGE_FETCH_TIMEOUT_MS
import com.example.nbaapp.domain.repository.TeamDetailRepository
import kotlinx.coroutines.withTimeoutOrNull

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
    suspend operator fun invoke(teamId: Int): Pair<Team, String> {
        val team = teamDetailRepository.getTeamDetail(teamId)
        val imageUrl = withTimeoutOrNull(IMAGE_FETCH_TIMEOUT_MS) {
            imageRepository.getImageUrl("${team.fullName} NBA team")
        } ?: DEFAULT_IMAGE_URL

        return Pair(team, imageUrl)
    }

}