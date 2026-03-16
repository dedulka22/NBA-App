package com.example.nbaapp.data.model

import com.example.nbaapp.domain.model.Player
import com.example.nbaapp.domain.model.PlayerDetail
import com.google.gson.annotations.SerializedName

/**
 * Data class for player response
 * @param meta The meta DTO
 * @param data The player DTO
 */
data class PlayerResponse(
    val meta: MetaDto,
    val data: List<PlayerDto>
)

/**
 * Data class for meta DTO
 * @param nextCursor The next cursor
 * @param perPage The number of items per page
 */
data class MetaDto(
    @SerializedName("next_cursor") val nextCursor: Int,
    @SerializedName("per_page") val perPage: Int
)

/**
 * Data class for player DTO
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
data class PlayerDto(
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
) {
    fun toDomain(): Player {
        return Player(
            id = id,
            firstName = firstName,
            lastName = lastName,
            position = position.orEmpty(),
            team = team.toDomain(),
        )
    }

    fun toDetailDomain(): PlayerDetail {
        return PlayerDetail(
            id = id,
            firstName = firstName,
            lastName = lastName,
            position = position.orEmpty(),
            height = height.orEmpty(),
            weight = weight.orEmpty(),
            jerseyNumber = jerseyNumber.orEmpty(),
            college = college.orEmpty(),
            country = country.orEmpty(),
            team = team.toDomain(),
            draftYear = draftYear ?: 0,
            draftRound = draftRound ?: 0,
            draftNumber = draftNumber ?: 0
        )
    }
}