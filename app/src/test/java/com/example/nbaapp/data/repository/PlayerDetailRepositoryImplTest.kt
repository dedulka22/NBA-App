package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.local.dao.PlayerDetailDao
import com.example.nbaapp.data.local.dao.TeamDao
import com.example.nbaapp.data.local.entity.PlayerDetailEntity
import com.example.nbaapp.data.model.PlayerDetailData
import com.example.nbaapp.data.model.PlayerDetailResponse
import com.example.nbaapp.data.model.TeamDto
import com.example.nbaapp.domain.model.Team
import com.example.nbaapp.domain.util.ConnectivityObserver
import com.example.nbaapp.domain.util.DataException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class PlayerDetailRepositoryTest {

    private lateinit var repository: PlayerDetailRepositoryImpl
    private lateinit var mockApi: NBAApi
    private lateinit var mockDao: PlayerDetailDao
    private lateinit var mockTeamDao: TeamDao
    private lateinit var mockTeamDto: TeamDto
    private lateinit var mockConnectivityObserver: ConnectivityObserver

    private val cachedEntity = PlayerDetailEntity(
        id = 2,
        firstName = "John",
        lastName = "Doe",
        position = "Guard",
        height = "180",
        weight = "80",
        college = "College A",
        jerseyNumber = "1",
        country = "USA",
        teamId = 1,
        teamAbbreviation = "TA",
        teamConference = "Conference A",
        teamDivision = "Division A",
        teamCity = "City A",
        teamName = "Team A",
        teamFullName = "Team A (TA)",
        draftYear = 1990,
        draftRound = 1,
        draftNumber = 16
    )

    @Before
    fun setup() {
        mockApi = mockk()
        mockDao = mockk(relaxed = true)
        mockTeamDao = mockk(relaxed = true)
        mockTeamDto = mockk()
        mockConnectivityObserver = mockk()
        every { mockConnectivityObserver.isCurrentlyConnected } returns true
        repository = PlayerDetailRepositoryImpl(mockApi, mockDao, mockTeamDao, mockConnectivityObserver)
    }

    @Test
    fun shouldReturnCachedDataWithoutApiCall() = runTest {
        // Arrange
        val playerId = 2
        coEvery { mockDao.getById(playerId) } returns cachedEntity

        // Act
        val result = repository.getPlayerDetail(playerId)

        // Assert
        assertEquals("John", result.firstName)
        assertEquals("Doe", result.lastName)
        coVerify(exactly = 0) { mockApi.getPlayerById(any()) }
    }

    @Test
    fun shouldFetchFromApiWhenNoCachedData() = runTest {
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
        val mockTeam = Team(
            id = 1,
            name = "Team A",
            abbreviation = "TA",
            city = "City A",
            conference = "Conference A",
            division = "Division A",
            fullName = "Team A (TA)"
        )

        coEvery { mockDao.getById(playerId) } returns null
        coEvery { mockApi.getPlayerById(playerId) } returns mockPlayerDetailResponse
        every { mockTeamDto.toDomain() } returns mockTeam

        // Act
        val result = repository.getPlayerDetail(playerId)

        // Assert
        assertEquals("John", result.firstName)
        assertEquals("Doe", result.lastName)
        assertEquals("Guard", result.position)
        assertEquals(mockTeam, result.team)
        coVerify { mockDao.insert(any()) }
        coVerify { mockTeamDao.insert(any()) }
    }

    @Test
    fun shouldThrowNetworkExceptionWhenNoCacheAndApiFailsAndOnline() = runTest {
        // Arrange
        val playerId = 2
        coEvery { mockDao.getById(playerId) } returns null
        coEvery { mockApi.getPlayerById(playerId) } throws IOException("Network error")

        // Act & Assert
        try {
            repository.getPlayerDetail(playerId)
            assert(false) { "Expected DataException.Network to be thrown" }
        } catch (e: DataException.Network) {
            assertEquals("Network error", e.message)
        }
    }

    @Test
    fun shouldReturnCachedDataWhenOffline() = runTest {
        // Arrange
        val playerId = 2
        every { mockConnectivityObserver.isCurrentlyConnected } returns false
        coEvery { mockDao.getById(playerId) } returns cachedEntity

        // Act
        val result = repository.getPlayerDetail(playerId)

        // Assert
        assertEquals("John", result.firstName)
        assertEquals("Doe", result.lastName)
        coVerify(exactly = 0) { mockApi.getPlayerById(any()) }
    }

    @Test
    fun shouldThrowNetworkExceptionWhenNoCacheAndOffline() = runTest {
        // Arrange
        val playerId = 2
        every { mockConnectivityObserver.isCurrentlyConnected } returns false
        coEvery { mockDao.getById(playerId) } returns null

        // Act & Assert
        try {
            repository.getPlayerDetail(playerId)
            assert(false) { "Expected DataException.Network to be thrown" }
        } catch (e: DataException.Network) {
            assertEquals("No network connection and no cached data", e.message)
        }
        coVerify(exactly = 0) { mockApi.getPlayerById(any()) }
    }

    @Test
    fun shouldThrowServerExceptionWhenNoCacheAndHttpException() = runTest {
        // Arrange
        val playerId = 2
        val httpException = HttpException(Response.error<Any>(429, ResponseBody.create(null, "")))
        coEvery { mockDao.getById(playerId) } returns null
        coEvery { mockApi.getPlayerById(playerId) } throws httpException

        // Act & Assert
        try {
            repository.getPlayerDetail(playerId)
            assert(false) { "Expected DataException.Server to be thrown" }
        } catch (e: DataException.Server) {
            assertEquals(429, e.code)
        }
    }
}
