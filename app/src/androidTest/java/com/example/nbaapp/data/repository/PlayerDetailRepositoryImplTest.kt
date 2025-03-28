package com.example.nbaapp.data

import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.model.PlayerDetailData
import com.example.nbaapp.data.model.PlayerDetailResponse
import com.example.nbaapp.data.model.TeamDto
import com.example.nbaapp.data.repository.PlayerDetailRepositoryImpl
import com.example.nbaapp.data.service.PlayerImageService
import com.example.nbaapp.domain.model.Team
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PlayerDetailRepositoryTest {

    private lateinit var repository: PlayerDetailRepositoryImpl
    private lateinit var service: PlayerImageService
    private lateinit var mockApi: NBAApi
    private lateinit var mockTeamDto: TeamDto

    @Before
    fun setup() {
        // Mocking dependencies
        mockApi = mockk()
        service = mockk()
        mockTeamDto = mockk()
        repository = PlayerDetailRepositoryImpl(mockApi, service)
    }

    @Test
    fun shouldReturnPlayerDetailsWhenApiCallIsSuccessful() = runTest {
        // Arrange
        val playerId = 2
        val mockPlayerDetailData = PlayerDetailData(
            id = playerId,
            firstName = "John",
            lastName = "Doe",
            position = "Guard",
            height = "180",
            weight = "80",
            college = "College A",
            jerseyNumber = "1",
            country = "USA",
            team = mockTeamDto,
            draftYear = 1990,
            draftRound = 1,
            draftNumber = 16
        )
        val mockPlayerDetailResponse = PlayerDetailResponse(data = mockPlayerDetailData)
        val mockTeam = Team(id = 1, name = "Team A", abbreviation = "TA", city = "City A", conference = "Conference A",
            division = "Division A", fullName = "Team A (TA)", image = "team_image")

        coEvery { mockApi.getPlayerById(playerId) } returns mockPlayerDetailResponse
        coEvery { service.getNBAImage(playerId) } returns "image_url"
        every { mockTeamDto.toDomain() } returns mockTeam

        // Act
        val result = repository.getPlayerDetail(playerId)

        // Assert
        assertEquals("John", result.firstName)
        assertEquals("Doe", result.lastName)
        assertEquals("Guard", result.position)
        assertEquals("180", result.height)
        assertEquals("80", result.weight)
        assertEquals("College A", result.college)
        assertEquals("1", result.jerseyNumber)
        assertEquals("USA", result.country)
        assertEquals(1990, result.draftYear)
        assertEquals(1, result.draftRound)
        assertEquals(16, result.draftNumber)
        assertEquals(mockTeam, result.team)
    }

    @Test
    fun shouldReturnNullWhenApiCallFails() = runTest {
        // Arrange
        val playerId = 2
        coEvery { mockApi.getPlayerById(playerId) } throws RuntimeException("Network error")
        coEvery { service.getNBAImage(playerId) } returns "image_url"

        // Act
        val result = try {
            repository.getPlayerDetail(playerId)
        } catch (e: RuntimeException) {
            null
        }

        // Assert
        assertNull(result)
    }
}