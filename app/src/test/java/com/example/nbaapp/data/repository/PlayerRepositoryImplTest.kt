package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.model.MetaDto
import com.example.nbaapp.data.model.PlayerDto
import com.example.nbaapp.data.model.PlayerResponse
import com.example.nbaapp.data.model.TeamDto
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerRepositoryImplTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(dispatcher)
    private lateinit var repository: PlayerRepositoryImpl
    private lateinit var mockApi: NBAApi

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockApi = mockk()
        repository = PlayerRepositoryImpl(mockApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cancel()
        unmockkAll()
    }

    @Test
    fun getPlayersShouldReturnPagingData() = runTest {
        testScope.launch {
            // Arrange
            val mockTeamA = TeamDto(
                id = 1,
                name = "Team A",
                abbreviation = "TA",
                city = "City A",
                conference = "Conference A",
                division = "Division A",
                fullName = "Team A (TA)"
            )
            val mockTeamB = TeamDto(
                id = 2,
                name = "Team B",
                abbreviation = "TB",
                city = "City B",
                conference = "Conference B",
                division = "Division B",
                fullName = "Team B (TB)"
            )
            val mockPlayers = listOf(
                PlayerDto(
                    id = 1,
                    firstName = "John",
                    lastName = "Doe",
                    position = "1",
                    team = mockTeamA
                ),
                PlayerDto(
                    id = 2,
                    firstName = "Jane",
                    lastName = "Smith",
                    position = "2",
                    team = mockTeamB
                )
            )
            val mockPlayerResponse = PlayerResponse(
                meta = MetaDto(
                    nextCursor = 1, perPage = 35
                ), data = mockPlayers
            )

            coEvery { mockApi.getPlayers(any(), any()) } returns mockPlayerResponse

            // Act
            val result = repository.getPlayers().toList()

            // Assert
            assertEquals(mockPlayers, result.first())
        }
    }
}
