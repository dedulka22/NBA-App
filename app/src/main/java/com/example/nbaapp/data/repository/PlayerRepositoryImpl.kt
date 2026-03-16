package com.example.nbaapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.local.NBADatabase
import com.example.nbaapp.data.local.entity.toDomain
import com.example.nbaapp.data.paging.PlayersRemoteMediator
import com.example.nbaapp.domain.model.Player
import com.example.nbaapp.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayerRepositoryImpl(
    private val api: NBAApi,
    private val database: NBADatabase
) : PlayerRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPlayers(): Flow<PagingData<Player>> {
        return Pager(
            config = PagingConfig(
                pageSize = NBAApi.DEFAULT_PER_PAGE,
                enablePlaceholders = false
            ),
            remoteMediator = PlayersRemoteMediator(api, database),
            pagingSourceFactory = { database.playerDao().getPlayers() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }
}
