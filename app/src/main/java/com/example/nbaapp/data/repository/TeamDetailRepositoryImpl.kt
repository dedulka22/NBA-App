package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.service.PlayerImageService
import com.example.nbaapp.domain.model.Team
import com.example.nbaapp.domain.repository.TeamDetailRepository

/**
 * Repository for team details
 * @param api The NBA API
 * @param playerImageService The player image service
 */
class TeamDetailRepositoryImpl(
    private val api: NBAApi,
    private val playerImageService: PlayerImageService
) : TeamDetailRepository {

    override suspend fun getTeamDetail(teamId: Int): Team {
        return try {
            val response = api.getTeamById(teamId)
            val image = playerImageService.getNBAImage(
                playerId = teamId,
            )
            response.data.toDomain(image)
        } catch (e: Exception) {
            throw e
        }
    }

}