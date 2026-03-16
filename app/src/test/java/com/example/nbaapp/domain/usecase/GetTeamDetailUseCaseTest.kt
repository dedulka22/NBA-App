package com.example.nbaapp.domain.usecase

import com.example.nbaapp.domain.model.Team
import com.example.nbaapp.domain.repository.ImageRepository
import com.example.nbaapp.domain.repository.TeamDetailRepository
import com.example.nbaapp.domain.repository.ImageRepository.Companion.DEFAULT_IMAGE_URL
import com.example.nbaapp.domain.util.DataException
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetTeamDetailUseCaseTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(dispatcher)
    private lateinit var useCase: GetTeamDetailUseCase
    private lateinit var mockTeamRepository: TeamDetailRepository
    private lateinit var mockImageRepository: ImageRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockTeamRepository = mockk()
        mockImageRepository = mockk()
        useCase = GetTeamDetailUseCase(mockTeamRepository, mockImageRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cancel()
        unmockkAll()
    }

    @Test
    fun invokeShouldReturnTeamUiModel() = runTest {
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
                fullName = "Team A (TA)"
            )
            val mockImageUrl = "https://example.com/team.jpg"

            coEvery { mockTeamRepository.getTeamDetail(teamId) } returns mockTeamDetail
            coEvery { mockImageRepository.getImageUrl(any()) } returns mockImageUrl

            // Act
            val (team, imageUrl) = useCase(teamId)

            // Assert
            assertEquals(mockTeamDetail.id, team.id)
            assertEquals(mockTeamDetail.name, team.name)
            assertEquals(mockTeamDetail.fullName, team.fullName)
            assertEquals(mockImageUrl, imageUrl)
        }
    }

    @Test
    fun shouldReturnDefaultImageUrlWhenImageFetchTimesOut() = runTest {
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
                fullName = "Team A (TA)"
            )

            coEvery { mockTeamRepository.getTeamDetail(teamId) } returns mockTeamDetail
            coEvery { mockImageRepository.getImageUrl(any()) } coAnswers {
                delay(3000L)
                "https://example.com/team.jpg"
            }

            // Act
            val (team, imageUrl) = useCase(teamId)

            // Assert
            assertEquals(mockTeamDetail.id, team.id)
            assertEquals(DEFAULT_IMAGE_URL, imageUrl)
        }
    }

    @Test
    fun shouldPropagateRepositoryException() = runTest {
        testScope.launch {
            // Arrange
            val teamId = 1
            coEvery { mockTeamRepository.getTeamDetail(teamId) } throws DataException.Network()

            // Act & Assert
            assertThrows(DataException.Network::class.java) {
                kotlinx.coroutines.test.runTest {
                    useCase(teamId)
                }
            }
        }
    }
}