package com.example.nbaapp.data.model

import com.example.nbaapp.domain.model.PlayerDetail
import com.example.nbaapp.domain.model.Team
import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerDetailResponseMappingTest {

    private val teamDto = TeamDto(
        id = 1,
        conference = "West",
        division = "Pacific",
        city = "LA",
        name = "Lakers",
        fullName = "Los Angeles Lakers",
        abbreviation = "LAL"
    )

    private val expectedTeam = Team(
        id = 1,
        conference = "West",
        division = "Pacific",
        city = "LA",
        name = "Lakers",
        fullName = "Los Angeles Lakers",
        abbreviation = "LAL"
    )

    @Test
    fun shouldMapResponseToDomainCorrectly() {
        val response = PlayerDetailResponse(
            data = PlayerDetailData(
                id = 42,
                firstName = "LeBron",
                lastName = "James",
                position = "F",
                height = "6-9",
                weight = "250",
                jerseyNumber = "23",
                college = "St. Vincent-St. Mary",
                country = "USA",
                team = teamDto,
                draftYear = 2003,
                draftRound = 1,
                draftNumber = 1
            )
        )

        val result: PlayerDetail = response.toDomain()

        assertEquals(42, result.id)
        assertEquals("LeBron", result.firstName)
        assertEquals("James", result.lastName)
        assertEquals("F", result.position)
        assertEquals("6-9", result.height)
        assertEquals("250", result.weight)
        assertEquals("23", result.jerseyNumber)
        assertEquals("St. Vincent-St. Mary", result.college)
        assertEquals("USA", result.country)
        assertEquals(expectedTeam, result.team)
        assertEquals(2003, result.draftYear)
        assertEquals(1, result.draftRound)
        assertEquals(1, result.draftNumber)
    }

    @Test
    fun shouldHandleNullFieldsInResponseToDomain() {
        val response = PlayerDetailResponse(
            data = PlayerDetailData(
                id = 99,
                firstName = "Test",
                lastName = "Player",
                position = null,
                height = null,
                weight = null,
                jerseyNumber = null,
                college = null,
                country = null,
                team = teamDto,
                draftYear = null,
                draftRound = null,
                draftNumber = null
            )
        )

        val result: PlayerDetail = response.toDomain()

        assertEquals(99, result.id)
        assertEquals("Test", result.firstName)
        assertEquals("Player", result.lastName)
        assertEquals("", result.position)
        assertEquals("", result.height)
        assertEquals("", result.weight)
        assertEquals("", result.jerseyNumber)
        assertEquals("", result.college)
        assertEquals("", result.country)
        assertEquals(expectedTeam, result.team)
        assertEquals(0, result.draftYear)
        assertEquals(0, result.draftRound)
        assertEquals(0, result.draftNumber)
    }
}
