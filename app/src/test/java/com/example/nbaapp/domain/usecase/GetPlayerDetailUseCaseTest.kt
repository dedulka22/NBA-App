package com.example.nbaapp.domain.usecase

import com.example.nbaapp.domain.model.PlayerDetail
import com.example.nbaapp.domain.repository.ImageRepository
import com.example.nbaapp.domain.repository.PlayerDetailRepository
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
class GetPlayerDetailUseCaseTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(dispatcher)
    private lateinit var useCase: GetPlayerDetailUseCase
    private lateinit var mockPlayerRepository: PlayerDetailRepository
    private lateinit var mockImageRepository: ImageRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockPlayerRepository = mockk()
        mockImageRepository = mockk()
        useCase = GetPlayerDetailUseCase(mockPlayerRepository, mockImageRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cancel()
        unmockkAll()
    }

    @Test
    fun invokeShouldReturnPlayerDetailUiModel() = runTest {
        testScope.launch {
            // Arrange
            val playerId = 1
            val mockPlayerDetail = PlayerDetail(
                id = playerId,
                firstName = "John",
                lastName = "Doe",
                position = "Guard",
                height = "180",
                weight = "80",
                college = "College A",
                jerseyNumber = "1",
                country = "USA",
                team = mockk(),
                draftYear = 1990,
                draftRound = 1,
                draftNumber = 16
            )
            val mockImageUrl = "https://example.com/player.jpg"

            coEvery { mockPlayerRepository.getPlayerDetail(playerId) } returns mockPlayerDetail
            coEvery { mockImageRepository.getImageUrl(any()) } returns mockImageUrl

            // Act
            val result = useCase(playerId)

            // Assert
            assertEquals(mockPlayerDetail.id, result.id)
            assertEquals(mockPlayerDetail.firstName, result.firstName)
            assertEquals(mockPlayerDetail.lastName, result.lastName)
            assertEquals(mockImageUrl, result.imageUrl)
        }
    }
}