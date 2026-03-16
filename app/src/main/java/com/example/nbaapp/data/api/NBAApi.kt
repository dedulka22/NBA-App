package com.example.nbaapp.data.api

import com.example.nbaapp.data.model.PlayerDetailResponse
import com.example.nbaapp.data.model.PlayerResponse
import com.example.nbaapp.data.model.TeamResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * NBA API interface
 */
interface NBAApi {

    @GET(ENDPOINT_PLAYERS)
    suspend fun getPlayers(
        @Query(PARAM_CURSOR) cursor: Int,
        @Query(PARAM_PER_PAGE) perPage: Int = DEFAULT_PER_PAGE,
    ): PlayerResponse

    @GET(ENDPOINT_PLAYER_DETAIL)
    suspend fun getPlayerById(
        @Path(PARAM_ID) id: Int
    ): PlayerDetailResponse

    @GET(ENDPOINT_TEAM_DETAIL)
    suspend fun getTeamById(
        @Path(PARAM_ID) id: Int
    ): TeamResponse

    companion object {
        const val ENDPOINT_PLAYERS = "players"
        const val ENDPOINT_PLAYER_DETAIL = "players/{id}"
        const val ENDPOINT_TEAM_DETAIL = "teams/{id}"
        const val PARAM_CURSOR = "cursor"
        const val PARAM_PER_PAGE = "per_page"
        const val PARAM_ID = "id"
        const val DEFAULT_PER_PAGE = 100
    }
}