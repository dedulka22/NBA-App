package com.example.nbaapp.domain.repository

import androidx.paging.PagingData
import com.example.nbaapp.domain.model.Player
import kotlinx.coroutines.flow.Flow

/**
 * Repository for player data
 */
interface PlayerRepository {
    fun getPlayers(): Flow<PagingData<Player>>
}