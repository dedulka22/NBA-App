package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.model.toDomain
import com.example.nbaapp.data.service.PlayerImageService
import com.example.nbaapp.domain.model.PlayerDetail
import com.example.nbaapp.domain.repository.PlayerDetailRepository

/**
 * Repository for player details
 * @param api The NBA API
 * @param playerImageService The player image service
 */
class PlayerDetailRepositoryImpl(
    private val api: NBAApi,
    private val playerImageService: PlayerImageService
) : PlayerDetailRepository {

    override suspend fun getPlayerDetail(playerId: Int): PlayerDetail {
        return try {
            val response = api.getPlayerById(playerId)
            val urlImage = playerImageService.getNBAImage(
                playerId = playerId
            )
            response.toDomain(urlImage)
        } catch (e: Exception) {
            throw e
        }
    }
}