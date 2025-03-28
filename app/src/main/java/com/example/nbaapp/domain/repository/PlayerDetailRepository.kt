package com.example.nbaapp.domain.repository

import com.example.nbaapp.domain.model.PlayerDetail

/**
 * Repository for player details
 */
interface PlayerDetailRepository {
    suspend fun getPlayerDetail(playerId: Int): PlayerDetail
}