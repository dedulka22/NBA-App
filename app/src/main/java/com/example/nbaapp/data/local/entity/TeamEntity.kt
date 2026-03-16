package com.example.nbaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nbaapp.domain.model.Team

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey val id: Int,
    val abbreviation: String,
    val conference: String,
    val division: String,
    val city: String,
    val name: String,
    val fullName: String
)

fun TeamEntity.toDomain(): Team = Team(
    id = id,
    abbreviation = abbreviation,
    conference = conference,
    division = division,
    city = city,
    name = name,
    fullName = fullName
)

fun Team.toEntity(): TeamEntity = TeamEntity(
    id = id,
    abbreviation = abbreviation,
    conference = conference,
    division = division,
    city = city,
    name = name,
    fullName = fullName
)
