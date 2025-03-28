package com.example.nbaapp.data.model

import com.example.nbaapp.domain.model.Team
import com.google.gson.annotations.SerializedName

/**
 * Data class for team response
 * @param data The team DTO
 */
data class TeamResponse(
    val data: TeamDto
)

/**
 * Data class for team DTO
 * @param id The team ID
 * @param conference The team conference
 * @param division The team division
 * @param city The team city
 * @param name The team name
 * @param fullName The team full name
 * @param abbreviation The team abbreviation
 */
data class TeamDto(
    val id: Int,
    val conference: String,
    val division: String,
    val city: String,
    val name: String,
    @SerializedName("full_name") val fullName: String,
    val abbreviation: String
) {
    fun toDomain(image: String? = ""): Team {
        return Team(
            id = id,
            conference = conference,
            division = division,
            abbreviation = abbreviation,
            city = city,
            name = name,
            fullName = fullName,
            image = image ?: ""
        )
    }
}
