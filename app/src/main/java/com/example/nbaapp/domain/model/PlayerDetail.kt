package com.example.nbaapp.domain.model

/**
 * Domain model for player details.
 * This model represents pure business data without UI-specific properties.
 *
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
    val draftNumber: Int
)
