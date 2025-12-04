package com.example.nbaapp.domain.model

/**
 * Domain model for team details.
 * This model represents pure business data without UI-specific properties.
 *
 * @param id The team ID
 * @param abbreviation The team abbreviation
 * @param conference The team conference
 * @param division The team division
 * @param city The team city
 * @param name The team name
 * @param fullName The team full name
 */
data class Team(
    val id: Int,
    val abbreviation: String,
    val conference: String,
    val division: String,
    val city: String,
    val name: String,
    val fullName: String
)