package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.local.dao.TeamDao
import com.example.nbaapp.data.local.entity.toDomain
import com.example.nbaapp.data.local.entity.toEntity
import com.example.nbaapp.domain.model.Team
import com.example.nbaapp.domain.repository.TeamDetailRepository
import com.example.nbaapp.domain.util.ConnectivityObserver
import com.example.nbaapp.domain.util.DataException
import retrofit2.HttpException
import java.io.IOException

class TeamDetailRepositoryImpl(
    private val api: NBAApi,
    private val teamDao: TeamDao,
    private val connectivityObserver: ConnectivityObserver
) : TeamDetailRepository {

    override suspend fun getTeamDetail(teamId: Int): Team {
        val cached = teamDao.getById(teamId)?.toDomain()
        if (cached != null) return cached

        if (!connectivityObserver.isCurrentlyConnected) {
            throw DataException.Network("No network connection and no cached data")
        }

        try {
            val response = api.getTeamById(teamId)
            val domain = response.data.toDomain()
            teamDao.insert(domain.toEntity())
            return domain
        } catch (e: IOException) {
            throw DataException.Network(e.message ?: "Network error")
        } catch (e: HttpException) {
            throw DataException.Server(e.code(), e.message())
        }
    }
}
