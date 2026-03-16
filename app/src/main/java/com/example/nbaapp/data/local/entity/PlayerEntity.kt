package com.example.nbaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nbaapp.domain.model.Player
import com.example.nbaapp.domain.model.Team

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey val id: Int,
    val firstName: String,
    val lastName: String,
    val position: String,
    val teamId: Int,
    val teamAbbreviation: String,
    val teamConference: String,
    val teamDivision: String,
    val teamCity: String,
    val teamName: String,
    val teamFullName: String
)

fun PlayerEntity.toDomain(): Player = Player(
    id = id,
    firstName = firstName,
    lastName = lastName,
    position = position,
    team = Team(
        id = teamId,
        abbreviation = teamAbbreviation,
        conference = teamConference,
        division = teamDivision,
        city = teamCity,
        name = teamName,
        fullName = teamFullName
    )
)

fun Player.toEntity(): PlayerEntity = PlayerEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    position = position,
    teamId = team.id,
    teamAbbreviation = team.abbreviation,
    teamConference = team.conference,
    teamDivision = team.division,
    teamCity = team.city,
    teamName = team.name,
    teamFullName = team.fullName
)
