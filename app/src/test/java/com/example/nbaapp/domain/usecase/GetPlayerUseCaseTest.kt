package com.example.nbaapp.domain.usecase

import androidx.paging.PagingData
import com.example.nbaapp.domain.model.Player
import com.example.nbaapp.domain.repository.PlayerRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
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
class GetPlayersUseCaseTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(dispatcher)
    private lateinit var useCase: GetPlayersUseCase
    private lateinit var mockRepository: PlayerRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockRepository = mockk()
        useCase = GetPlayersUseCase(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cancel()
        unmockkAll()
    }

    @Test
    fun invokeShouldReturnPagingDataOfPlayers() = runTest {
        testScope.launch {
            // Arrange
            val mockPlayers = listOf(
                Player(id = 1, firstName = "John", lastName = "Doe", position = "Guard", team = mockk()),
                Player(id = 2, firstName = "Jane", lastName = "Smith", position = "Forward", team = mockk())
            )
            val mockPagingData = PagingData.from(mockPlayers)
            coEvery { mockRepository.getPlayers() } returns flowOf(mockPagingData)

            // Act
            val result = useCase().toList()

            // Assert
            assertEquals(mockPagingData, result.first())
        }
    }
}