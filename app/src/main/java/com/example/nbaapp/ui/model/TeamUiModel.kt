package com.example.nbaapp.ui.model

import com.example.nbaapp.domain.model.Team

/**
 * UI model for team details.
 * This model extends the domain model with UI-specific properties like image URL.
 *
 * @param team The domain team data
 * @param imageUrl The URL of the team's image
 */
data class TeamUiModel(
    val team: Team,
    val imageUrl: String
) {
    val id: Int get() = team.id
    val abbreviation: String get() = team.abbreviation
    val conference: String get() = team.conference
    val division: String get() = team.division
    val city: String get() = team.city
    val name: String get() = team.name
    val fullName: String get() = team.fullName
}
