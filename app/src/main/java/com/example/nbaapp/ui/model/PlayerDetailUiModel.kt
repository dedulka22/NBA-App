package com.example.nbaapp.ui.model

import com.example.nbaapp.domain.model.PlayerDetail

/**
 * UI model for player details.
 * This model extends the domain model with UI-specific properties like image URL.
 *
 * @param playerDetail The domain player detail data
 * @param imageUrl The URL of the player's image
 */
data class PlayerDetailUiModel(
    val playerDetail: PlayerDetail,
    val imageUrl: String
) {
    val id: Int get() = playerDetail.id
    val firstName: String get() = playerDetail.firstName
    val lastName: String get() = playerDetail.lastName
    val fullName: String get() = "$firstName $lastName"
    val position: String get() = playerDetail.position
    val height: String get() = playerDetail.height
    val weight: String get() = playerDetail.weight
    val college: String get() = playerDetail.college
    val jerseyNumber: String get() = playerDetail.jerseyNumber
    val country: String get() = playerDetail.country
    val team: TeamUiModel get() = TeamUiModel(playerDetail.team, imageUrl = imageUrl)
    val draftYear: Int get() = playerDetail.draftYear
    val draftRound: Int get() = playerDetail.draftRound
    val draftNumber: Int get() = playerDetail.draftNumber
}
