package com.example.nbaapp.domain.repository

import com.example.nbaapp.domain.model.Team

/**
 * Repository for team details
 */
interface TeamDetailRepository {
    suspend fun getTeamDetail(teamId: Int): Team
}