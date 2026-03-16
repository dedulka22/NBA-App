package com.example.nbaapp.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.nbaapp.R
import com.example.nbaapp.domain.model.PlayerDetail
import com.example.nbaapp.domain.model.Team
import com.example.nbaapp.domain.usecase.GetPlayerDetailUseCase
import com.example.nbaapp.domain.util.ConnectivityObserver
import com.example.nbaapp.domain.util.DataException
import com.example.nbaapp.ui.model.PlayerDetailUiModel
import com.example.nbaapp.ui.util.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PlayerDetailViewModelTest {

    private lateinit var mockGetPlayerDetailUseCase: GetPlayerDetailUseCase
    private lateinit var mockConnectivityObserver: ConnectivityObserver
    private lateinit var connectivityFlow: MutableStateFlow<Boolean>

    private val testDispatcher = UnconfinedTestDispatcher()

    private val testTeam = Team(
        id = 1,
        abbreviation = "LAL",
        conference = "West",
        division = "Pacific",
        city = "Los Angeles",
        name = "Lakers",
        fullName = "Los Angeles Lakers"
    )

    private val testPlayerDetail = PlayerDetail(
        id = 1,
        firstName = "LeBron",
        lastName = "James",
        position = "F",
        height = "6-9",
        weight = "250",
        college = "None",
        jerseyNumber = "23",
        country = "USA",
        team = testTeam,
        draftYear = 2003,
        draftRound = 1,
        draftNumber = 1
    )

    private val testImageUrl = "https://example.com/image.jpg"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockGetPlayerDetailUseCase = mockk()
        mockConnectivityObserver = mockk()
        connectivityFlow = MutableStateFlow(true)
        every { mockConnectivityObserver.isConnected } returns connectivityFlow
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): PlayerDetailViewModel {
        val savedStateHandle = SavedStateHandle(mapOf("playerId" to 1))
        return PlayerDetailViewModel(
            getPlayerDetailUseCase = mockGetPlayerDetailUseCase,
            savedStateHandle = savedStateHandle,
            connectivityObserver = mockConnectivityObserver
        )
    }

    @Test
    fun shouldEmitLoadingThenSuccessOnLoad() = runTest {
        // Arrange
        coEvery { mockGetPlayerDetailUseCase(1) } returns Pair(testPlayerDetail, testImageUrl)

        // Act
        val viewModel = createViewModel()

        // Assert
        val state = viewModel.playerDetail.value
        assertTrue(state is UiState.Success)
        val successState = state as UiState.Success<PlayerDetailUiModel>
        assertEquals("LeBron", successState.data.firstName)
        assertEquals("James", successState.data.lastName)
        assertEquals(testImageUrl, successState.data.imageUrl)
    }

    @Test
    fun shouldEmitNetworkErrorOnNetworkException() = runTest {
        // Arrange
        coEvery { mockGetPlayerDetailUseCase(1) } throws DataException.Network("Network error")

        // Act
        val viewModel = createViewModel()

        // Assert
        val state = viewModel.playerDetail.value
        assertTrue(state is UiState.Error)
        val errorState = state as UiState.Error
        assertEquals(R.string.error_network, errorState.messageResId)
    }

    @Test
    fun shouldEmitServerErrorOnServerException() = runTest {
        // Arrange
        coEvery { mockGetPlayerDetailUseCase(1) } throws DataException.Server(429)

        // Act
        val viewModel = createViewModel()

        // Assert
        val state = viewModel.playerDetail.value
        assertTrue(state is UiState.Error)
        val errorState = state as UiState.Error
        assertEquals(R.string.error_server, errorState.messageResId)
        assertEquals("429", errorState.formatArgs.first())
    }

    @Test
    fun shouldRetryOnRetryCall() = runTest {
        // Arrange - first call fails, second succeeds
        coEvery { mockGetPlayerDetailUseCase(1) } throws DataException.Network("Network error")

        val viewModel = createViewModel()
        assertTrue(viewModel.playerDetail.value is UiState.Error)

        // Arrange - now make it succeed on retry
        coEvery { mockGetPlayerDetailUseCase(1) } returns Pair(testPlayerDetail, testImageUrl)

        // Act
        viewModel.retry()

        // Assert
        val state = viewModel.playerDetail.value
        assertTrue(state is UiState.Success)
        coVerify(exactly = 2) { mockGetPlayerDetailUseCase(1) }
    }

    @Test
    fun shouldReloadOnConnectivityRestored() = runTest {
        // Arrange - initial load fails
        coEvery { mockGetPlayerDetailUseCase(1) } throws DataException.Network("Network error")

        val viewModel = createViewModel()
        assertTrue(viewModel.playerDetail.value is UiState.Error)

        // Arrange - now make it succeed
        coEvery { mockGetPlayerDetailUseCase(1) } returns Pair(testPlayerDetail, testImageUrl)

        // Act - simulate connectivity restored (drop(1) skips the initial value,
        // so we need to change the value to trigger collection)
        connectivityFlow.value = false
        connectivityFlow.value = true

        // Assert
        val state = viewModel.playerDetail.value
        assertTrue(state is UiState.Success)
        val successState = state as UiState.Success<PlayerDetailUiModel>
        assertEquals("LeBron", successState.data.firstName)
    }
}
