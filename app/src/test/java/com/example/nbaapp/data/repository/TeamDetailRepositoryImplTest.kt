package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.local.dao.TeamDao
import com.example.nbaapp.data.local.entity.TeamEntity
import com.example.nbaapp.data.model.TeamDto
import com.example.nbaapp.data.model.TeamResponse
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
class TeamDetailRepositoryImplTest {

    private lateinit var repository: TeamDetailRepositoryImpl
    private lateinit var mockApi: NBAApi
    private lateinit var mockDao: TeamDao
    private lateinit var mockConnectivityObserver: ConnectivityObserver

    @Before
    fun setup() {
        mockApi = mockk()
        mockDao = mockk(relaxed = true)
        mockConnectivityObserver = mockk()
        every { mockConnectivityObserver.isCurrentlyConnected } returns true
        repository = TeamDetailRepositoryImpl(mockApi, mockDao, mockConnectivityObserver)
    }

    @Test
    fun shouldReturnCachedDataWithoutApiCall() = runTest {
        // Arrange
        val teamId = 1
        val cachedEntity = TeamEntity(
            id = teamId,
            abbreviation = "TA",
            conference = "Conference A",
            division = "Division A",
            city = "City A",
            name = "Team A",
            fullName = "Team A (TA)"
        )

        coEvery { mockDao.getById(teamId) } returns cachedEntity

        // Act
        val result = repository.getTeamDetail(teamId)

        // Assert
        assertEquals(teamId, result.id)
        assertEquals("Team A", result.name)
        assertEquals("TA", result.abbreviation)
        coVerify(exactly = 0) { mockApi.getTeamById(any()) }
    }

    @Test
    fun shouldFetchFromApiWhenNoCachedData() = runTest {
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

        coEvery { mockDao.getById(teamId) } returns null
        coEvery { mockApi.getTeamById(teamId) } returns mockTeamResponse

        // Act
        val result = repository.getTeamDetail(teamId)

        // Assert
        assertEquals(teamId, result.id)
        assertEquals("Team A", result.name)
        assertEquals("TA", result.abbreviation)
        coVerify { mockDao.insert(any()) }
    }

    @Test
    fun shouldThrowNetworkExceptionWhenNoCacheAndApiFailsAndOnline() = runTest {
        // Arrange
        val teamId = 1
        coEvery { mockDao.getById(teamId) } returns null
        coEvery { mockApi.getTeamById(teamId) } throws IOException("Network error")

        // Act & Assert
        try {
            repository.getTeamDetail(teamId)
            assert(false) { "Expected DataException.Network to be thrown" }
        } catch (e: DataException.Network) {
            assertEquals("Network error", e.message)
        }
    }

    @Test
    fun shouldThrowNetworkExceptionWhenNoCacheAndOffline() = runTest {
        // Arrange
        val teamId = 1
        every { mockConnectivityObserver.isCurrentlyConnected } returns false
        coEvery { mockDao.getById(teamId) } returns null

        // Act & Assert
        try {
            repository.getTeamDetail(teamId)
            assert(false) { "Expected DataException.Network to be thrown" }
        } catch (e: DataException.Network) {
            assertEquals("No network connection and no cached data", e.message)
        }
        coVerify(exactly = 0) { mockApi.getTeamById(any()) }
    }

    @Test
    fun shouldReturnCachedDataWhenOffline() = runTest {
        // Arrange
        val teamId = 1
        val cachedEntity = TeamEntity(
            id = teamId,
            abbreviation = "TA",
            conference = "Conference A",
            division = "Division A",
            city = "City A",
            name = "Team A",
            fullName = "Team A (TA)"
        )

        every { mockConnectivityObserver.isCurrentlyConnected } returns false
        coEvery { mockDao.getById(teamId) } returns cachedEntity

        // Act
        val result = repository.getTeamDetail(teamId)

        // Assert
        assertEquals(teamId, result.id)
        assertEquals("Team A", result.name)
        coVerify(exactly = 0) { mockApi.getTeamById(any()) }
    }

    @Test
    fun shouldThrowServerExceptionWhenNoCacheAndHttpException() = runTest {
        // Arrange
        val teamId = 1
        val httpException = HttpException(Response.error<Any>(429, ResponseBody.create(null, "")))
        coEvery { mockDao.getById(teamId) } returns null
        coEvery { mockApi.getTeamById(teamId) } throws httpException

        // Act & Assert
        try {
            repository.getTeamDetail(teamId)
            assert(false) { "Expected DataException.Server to be thrown" }
        } catch (e: DataException.Server) {
            assertEquals(429, e.code)
        }
    }
}
