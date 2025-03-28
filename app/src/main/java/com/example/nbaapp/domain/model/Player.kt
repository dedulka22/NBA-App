package com.example.nbaapp.domain.model

/**
 * Data class for player details
 * @param id The player ID
 * @param firstName The player first name
 * @param lastName The player last name
 * @param position The player position
 * @param team The player team
 */
data class Player(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val position: String,
    val team: Team
)