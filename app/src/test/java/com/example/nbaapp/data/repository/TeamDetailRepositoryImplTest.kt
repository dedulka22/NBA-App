package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.model.TeamDto
import com.example.nbaapp.data.model.TeamResponse
import com.example.nbaapp.domain.model.Team
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TeamDetailRepositoryImplTest {

    private lateinit var repository: TeamDetailRepositoryImpl
    private lateinit var mockApi: NBAApi

    @Before
    fun setup() {
        mockApi = mockk()
        repository = TeamDetailRepositoryImpl(mockApi)
    }

    @Test
    fun getTeamDetailShouldReturnTeam() = runTest {
        // Arrange
        val teamId = 1
        val mockTeamDto = TeamDto(
            id = teamId,
            name = "Team A",
            abbreviation = "TA",
            city = "City A",
            conference = "Conference A",
            division = "Division A",
            fullName = "Team A (TA)"
        )
        val mockTeamResponse = TeamResponse(data = mockTeamDto)

        coEvery { mockApi.getTeamById(teamId) } returns mockTeamResponse

        // Act
        val result = repository.getTeamDetail(teamId)

        // Assert
        assertEquals(teamId, result.id)
        assertEquals("Team A", result.name)
        assertEquals("TA", result.abbreviation)
        assertEquals("City A", result.city)
        assertEquals("Conference A", result.conference)
        assertEquals("Division A", result.division)
        assertEquals("Team A (TA)", result.fullName)
    }

    @Test
    fun getTeamDetailShouldThrowExceptionWhenApiFails() = runTest {
        // Arrange
        val teamId = 1
        coEvery { mockApi.getTeamById(teamId) } throws RuntimeException("Network error")

        // Act & Assert
        try {
            repository.getTeamDetail(teamId)
            assert(false) { "Expected RuntimeException to be thrown" }
        } catch (e: RuntimeException) {
            assertEquals("Network error", e.message)
        }
    }
}