package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.local.dao.PlayerDetailDao
import com.example.nbaapp.data.local.dao.TeamDao
import com.example.nbaapp.data.local.entity.toDomain
import com.example.nbaapp.data.local.entity.toEntity
import com.example.nbaapp.data.model.toDomain
import com.example.nbaapp.domain.model.PlayerDetail
import com.example.nbaapp.domain.repository.PlayerDetailRepository
import com.example.nbaapp.domain.util.ConnectivityObserver
import com.example.nbaapp.domain.util.DataException
import retrofit2.HttpException
import java.io.IOException

class PlayerDetailRepositoryImpl(
    private val api: NBAApi,
    private val playerDetailDao: PlayerDetailDao,
    private val teamDao: TeamDao,
    private val connectivityObserver: ConnectivityObserver
) : PlayerDetailRepository {

    override suspend fun getPlayerDetail(playerId: Int): PlayerDetail {
        val cached = playerDetailDao.getById(playerId)?.toDomain()
        if (cached != null) return cached

        if (!connectivityObserver.isCurrentlyConnected) {
            throw DataException.Network("No network connection and no cached data")
        }

        try {
            val response = api.getPlayerById(playerId)
            val domain = response.toDomain()
            playerDetailDao.insert(domain.toEntity())
            teamDao.insert(domain.team.toEntity())
            return domain
        } catch (e: IOException) {
            throw DataException.Network(e.message ?: "Network error")
        } catch (e: HttpException) {
            throw DataException.Server(e.code(), e.message())
        }
    }
}
