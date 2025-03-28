package com.example.nbaapp.domain.usecase

import androidx.paging.PagingData
import com.example.nbaapp.domain.model.Player
import com.example.nbaapp.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting players
 * @param repository The repository for player data
 */
class GetPlayersUseCase(
    private val repository: PlayerRepository
) {
    operator fun invoke(): Flow<PagingData<Player>> =
        repository.getPlayers()
}