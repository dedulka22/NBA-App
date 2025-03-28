package com.example.nbaapp.domain.usecase

import com.example.nbaapp.domain.repository.PlayerDetailRepository

/**
 * Use case for getting player details
 * @param repository The repository for player details
 */
class GetPlayerDetailUseCase(
    private val repository: PlayerDetailRepository
) {
    suspend operator fun invoke(id: Int) =
        repository.getPlayerDetail(id)
}