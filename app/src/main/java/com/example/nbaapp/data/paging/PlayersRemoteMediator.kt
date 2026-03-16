package com.example.nbaapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.local.NBADatabase
import com.example.nbaapp.data.local.entity.PlayerDetailEntity
import com.example.nbaapp.data.local.entity.PlayerEntity
import com.example.nbaapp.data.local.entity.RemoteKeyEntity
import com.example.nbaapp.data.local.entity.TeamEntity
import com.example.nbaapp.data.local.entity.toEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PlayersRemoteMediator(
    private val api: NBAApi,
    private val database: NBADatabase
) : RemoteMediator<Int, PlayerEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PlayerEntity>
    ): MediatorResult {
        val cursor = when (loadType) {
            LoadType.REFRESH -> FIRST_PAGE
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKey = database.remoteKeyDao().getKey(RemoteKeyEntity.PLAYERS_KEY)
                remoteKey?.nextCursor
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        return try {
            val perPage = state.config.pageSize.coerceAtMost(MAX_PER_PAGE)
            val response = api.getPlayers(cursor = cursor, perPage = perPage)
            val nextCursor = response.meta.nextCursor.takeIf { it > 0 }

            val playerEntities = ArrayList<PlayerEntity>(response.data.size)
            val detailEntities = ArrayList<PlayerDetailEntity>(response.data.size)
            val teamEntities = ArrayList<TeamEntity>(response.data.size)

            for (dto in response.data) {
                val player = dto.toDomain()
                val detail = dto.toDetailDomain()
                val team = dto.team.toDomain()
                playerEntities.add(player.toEntity())
                detailEntities.add(detail.toEntity())
                teamEntities.add(team.toEntity())
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.playerDao().clearAll()
                    database.remoteKeyDao().clearAll()
                }
                database.playerDao().insertAll(playerEntities)
                database.playerDetailDao().insertAll(detailEntities)
                database.teamDao().insertAll(teamEntities)
                database.remoteKeyDao().insert(
                    RemoteKeyEntity(RemoteKeyEntity.PLAYERS_KEY, nextCursor)
                )
            }

            MediatorResult.Success(endOfPaginationReached = nextCursor == null)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    companion object {
        const val FIRST_PAGE = 1
        const val MAX_PER_PAGE = 100
    }
}
