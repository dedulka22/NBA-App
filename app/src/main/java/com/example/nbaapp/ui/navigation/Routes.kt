package com.example.nbaapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object PlayersRoute

@Serializable
data class PlayerDetailRoute(val playerId: Int)

@Serializable
data class TeamDetailRoute(val teamId: Int)
