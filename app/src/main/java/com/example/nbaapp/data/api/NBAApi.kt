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

    @GET("players")
    suspend fun getPlayers(
        @Query("cursor") cursor: Int,
        @Query("per_page") perPage: Int = 35,
    ): PlayerResponse

    @GET("players/{id}")
    suspend fun getPlayerById(
        @Path("id") id: Int
    ): PlayerDetailResponse

    @GET("teams/{id}")
    suspend fun getTeamById(
        @Path("id") id: Int
    ): TeamResponse
}