package com.example.nbaapp.domain.model

/**
 * Data class for player details
 * @param id The player ID
 * @param firstName The player first name
 * @param lastName The player last name
 * @param position The player position
 * @param height The player height
 * @param weight The player weight
 * @param college The player college
 * @param jerseyNumber The player jersey number
 * @param country The player country
 * @param team The player team
 * @param draftYear The player draft year
 * @param draftRound The player draft round
 * @param draftNumber The player draft number
 * @param image The player image
 */
data class PlayerDetail(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val position: String,
    val height: String,
    val weight: String,
    val college: String,
    val jerseyNumber: String,
    val country: String,
    val team: Team,
    val draftYear: Int,
    val draftRound: Int,
    val draftNumber: Int,
    val image: String
)
