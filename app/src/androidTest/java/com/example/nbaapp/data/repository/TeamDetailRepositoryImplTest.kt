package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.model.TeamDto
import com.example.nbaapp.data.model.TeamResponse
import com.example.nbaapp.data.service.PlayerImageService
import com.example.nbaapp.domain.model.Team
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TeamDetailRepositoryImplTest {

    private lateinit var repository: TeamDetailRepositoryImpl
    private lateinit var mockApi: NBAApi
    private lateinit var mockPlayerImageService: PlayerImageService

    @Before
    fun setup() {
        mockApi = mockk()
        mockPlayerImageService = mockk()
        repository = TeamDetailRepositoryImpl(mockApi, mockPlayerImageService)
    }

    @Test
    fun getTeamDetailShouldReturnTeam() = runTest {
        // Arrange
        val teamId = 1
        val mockTeamDto = TeamDto(id = teamId, name = "Team A", abbreviation = "TA", city = "City A", conference = "Conference A",
            division = "Division A", fullName = "Team A (TA)")
        val mockTeamResponse = TeamResponse(data = mockTeamDto)
        val mockTeam = Team(id = teamId, name = "Team A", abbreviation = "TA", city = "City A", conference = "Conference A",
            division = "Division A", fullName = "Team A (TA)", image = "team_image")

        coEvery { mockApi.getTeamById(teamId) } returns mockTeamResponse
        coEvery { mockPlayerImageService.getNBAImage(teamId) } returns "team_image"
        coEvery { mockk<TeamDto>().toDomain("team_image") } returns mockTeam

        // Act
        val result = repository.getTeamDetail(teamId)

        // Assert
        assertEquals(mockTeam, result)
    }

    @Test
    fun getTeamDetailShouldThrowExceptionWhenApiFails() = runTest {
        // Arrange
        val teamId = 1
        coEvery { mockApi.getTeamById(teamId) } throws RuntimeException("Network error")
        coEvery { mockPlayerImageService.getNBAImage(teamId) } returns "team_image"

        // Act & Assert
        assertThrows(RuntimeException::class.java) {
            runTest {
                repository.getTeamDetail(teamId)
            }
        }
    }
}