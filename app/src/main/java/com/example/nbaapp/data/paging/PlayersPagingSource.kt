package com.example.nbaapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.domain.model.Player

/**
 * Paging source for players
 * @param api NBA API
 */
class PlayersPagingSource(
    private val api: NBAApi
) : PagingSource<Int, Player>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Player> {
        return try {
            val page = params.key ?: 1

            val response = api.getPlayers(
                cursor = page,
                perPage = params.loadSize
            )

            val players = response.data.map { it.toDomain() }

            LoadResult.Page(
                data = players,
                prevKey = if (page == 1) null else page - 1,
                nextKey = response.meta.nextCursor.takeIf { it > 0 }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Player>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
