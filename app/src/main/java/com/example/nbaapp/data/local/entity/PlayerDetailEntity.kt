package com.example.nbaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nbaapp.domain.model.PlayerDetail
import com.example.nbaapp.domain.model.Team

@Entity(tableName = "player_details")
data class PlayerDetailEntity(
    @PrimaryKey val id: Int,
    val firstName: String,
    val lastName: String,
    val position: String,
    val height: String,
    val weight: String,
    val college: String,
    val jerseyNumber: String,
    val country: String,
    val teamId: Int,
    val teamAbbreviation: String,
    val teamConference: String,
    val teamDivision: String,
    val teamCity: String,
    val teamName: String,
    val teamFullName: String,
    val draftYear: Int,
    val draftRound: Int,
    val draftNumber: Int
)

fun PlayerDetailEntity.toDomain(): PlayerDetail = PlayerDetail(
    id = id,
    firstName = firstName,
    lastName = lastName,
    position = position,
    height = height,
    weight = weight,
    college = college,
    jerseyNumber = jerseyNumber,
    country = country,
    team = Team(
        id = teamId,
        abbreviation = teamAbbreviation,
        conference = teamConference,
        division = teamDivision,
        city = teamCity,
        name = teamName,
        fullName = teamFullName
    ),
    draftYear = draftYear,
    draftRound = draftRound,
    draftNumber = draftNumber
)

fun PlayerDetail.toEntity(): PlayerDetailEntity = PlayerDetailEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    position = position,
    height = height,
    weight = weight,
    college = college,
    jerseyNumber = jerseyNumber,
    country = country,
    teamId = team.id,
    teamAbbreviation = team.abbreviation,
    teamConference = team.conference,
    teamDivision = team.division,
    teamCity = team.city,
    teamName = team.name,
    teamFullName = team.fullName,
    draftYear = draftYear,
    draftRound = draftRound,
    draftNumber = draftNumber
)
