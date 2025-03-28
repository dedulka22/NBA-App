package com.example.nbaapp.data.model

import com.example.nbaapp.domain.model.Player
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
 * @param team The player team DTO
 */
data class PlayerDto(
    val id: Int,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val position: String,
    val team: TeamDto
) {
    fun toDomain(): Player {
        return Player(
            id = id,
            firstName = firstName,
            lastName = lastName,
            position = position,
            team = team.toDomain(),
        )
    }
}