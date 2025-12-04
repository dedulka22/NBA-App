package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.domain.model.Team
import com.example.nbaapp.domain.repository.TeamDetailRepository

/**
 * Repository implementation for team details.
 *
 * @param api The NBA API interface
 */
class TeamDetailRepositoryImpl(
    private val api: NBAApi
) : TeamDetailRepository {

    override suspend fun getTeamDetail(teamId: Int): Team {
        val response = api.getTeamById(teamId)
        return response.data.toDomain()
    }
}