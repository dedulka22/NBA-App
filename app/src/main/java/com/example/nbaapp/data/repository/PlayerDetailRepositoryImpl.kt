package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.model.toDomain
import com.example.nbaapp.domain.model.PlayerDetail
import com.example.nbaapp.domain.repository.PlayerDetailRepository

/**
 * Repository implementation for player details.
 *
 * @param api The NBA API interface
 */
class PlayerDetailRepositoryImpl(
    private val api: NBAApi
) : PlayerDetailRepository {

    override suspend fun getPlayerDetail(playerId: Int): PlayerDetail {
        val response = api.getPlayerById(playerId)
        return response.toDomain()
    }
}