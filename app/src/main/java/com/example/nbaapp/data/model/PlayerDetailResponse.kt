package com.example.nbaapp.data.model

import com.example.nbaapp.domain.model.PlayerDetail
import com.google.gson.annotations.SerializedName

/**
 * Data class for player detail response
 * @param data The player detail data
 */
data class PlayerDetailResponse(
    val data: PlayerDetailData
)

/**
 * Data class for player detail data
 * @param id The player ID
 * @param firstName The player first name
 * @param lastName The player last name
 * @param position The player position
 * @param height The player height
 * @param weight The player weight
 * @param jerseyNumber The player jersey number
 * @param college The player college
 * @param country The player country
 * @param team The player team DTO
 * @param draftYear The player draft year
 * @param draftRound The player draft round
 * @param draftNumber The player draft number
 */
data class PlayerDetailData(
    val id: Int,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val position: String?,
    val height: String?,
    val weight: String?,
    @SerializedName("jersey_number") val jerseyNumber: String?,
    val college: String?,
    val country: String?,
    val team: TeamDto,
    @SerializedName("draft_year") val draftYear: Int?,
    @SerializedName("draft_round") val draftRound: Int?,
    @SerializedName("draft_number") val draftNumber: Int?
)

/**
 * Converts PlayerDetailResponse to domain model PlayerDetail.
 */
fun PlayerDetailResponse.toDomain(): PlayerDetail {
    val playerData = this.data
    return PlayerDetail(
        id = playerData.id,
        firstName = playerData.firstName,
        lastName = playerData.lastName,
        position = playerData.position.orEmpty(),
        height = playerData.height.orEmpty(),
        weight = playerData.weight.orEmpty(),
        jerseyNumber = playerData.jerseyNumber.orEmpty(),
        college = playerData.college.orEmpty(),
        country = playerData.country.orEmpty(),
        team = playerData.team.toDomain(),
        draftYear = playerData.draftYear ?: 0,
        draftRound = playerData.draftRound ?: 0,
        draftNumber = playerData.draftNumber ?: 0
    )
}
