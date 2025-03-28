package com.example.nbaapp.domain.usecase

import com.example.nbaapp.domain.repository.TeamDetailRepository

/**
 * Use case for getting team details
 * @param repository The repository for team details
 */
class GetTeamDetailUseCase(
    private val repository: TeamDetailRepository
) {
    suspend operator fun invoke(id: Int) =
        repository.getTeamDetail(id)
}