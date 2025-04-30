package com.example.nbaapp.domain.usecase

import com.example.nbaapp.domain.model.Team
import com.example.nbaapp.domain.repository.TeamDetailRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
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
class GetTeamDetailUseCaseTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(dispatcher)
    private lateinit var useCase: GetTeamDetailUseCase
    private lateinit var mockRepository: TeamDetailRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockRepository = mockk()
        useCase = GetTeamDetailUseCase(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cancel()
        unmockkAll()
    }

    @Test
    fun invokeShouldReturnTeamDetail() = runTest {
        testScope.launch {
            // Arrange
            val teamId = 1
            val mockTeamDetail = Team(
                id = teamId,
                name = "Team A",
                city = "City A",
                abbreviation = "TA",
                conference = "Conference A",
                division = "Division A",
                fullName = "Team A (TA)",
                image = "team_image"
            )
            coEvery { mockRepository.getTeamDetail(teamId) } returns mockTeamDetail

            // Act
            val result = useCase(teamId)

            // Assert
            assertEquals(mockTeamDetail, result)
        }
    }
}