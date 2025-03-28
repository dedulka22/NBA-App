package com.example.nbaapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.paging.PlayersPagingSource
import com.example.nbaapp.domain.model.Player
import com.example.nbaapp.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

/**
 * Repository for player data
 * @param api NBA API
 */
class PlayerRepositoryImpl(
    private val api: NBAApi
) : PlayerRepository {

    override fun getPlayers(): Flow<PagingData<Player>> {
        return Pager(
            config = PagingConfig(
                pageSize = 35,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PlayersPagingSource(api) }
        ).flow
    }

}