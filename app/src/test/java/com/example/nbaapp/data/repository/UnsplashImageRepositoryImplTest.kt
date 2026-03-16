package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.UnsplashApi
import com.example.nbaapp.data.model.UnsplashImageDto
import com.example.nbaapp.data.model.UnsplashImageResponse
import com.example.nbaapp.data.model.UnsplashImageUrlsDto
import com.example.nbaapp.domain.repository.ImageRepository.Companion.DEFAULT_IMAGE_URL
import com.example.nbaapp.domain.util.ConnectivityObserver
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UnsplashImageRepositoryImplTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: UnsplashImageRepositoryImpl
    private lateinit var mockUnsplashApi: UnsplashApi
    private lateinit var mockConnectivityObserver: ConnectivityObserver

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockUnsplashApi = mockk()
        mockConnectivityObserver = mockk()
        every { mockConnectivityObserver.isCurrentlyConnected } returns true
        repository = UnsplashImageRepositoryImpl(mockUnsplashApi, mockConnectivityObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cancel()
        unmockkAll()
    }

    @Test
    fun shouldReturnImageUrlOnSuccess() = runTest {
        // Arrange
        val expectedUrl = "https://images.unsplash.com/photo-small.jpg"
        val response = UnsplashImageResponse(
            results = listOf(
                UnsplashImageDto(
                    id = "abc123",
                    urls = UnsplashImageUrlsDto(small = expectedUrl)
                )
            )
        )
        coEvery { mockUnsplashApi.showImageNBA(page = any(), query = any()) } returns response

        // Act
        val result = repository.getImageUrl("LeBron James basketball")

        // Assert
        assertEquals(expectedUrl, result)
    }

    @Test
    fun shouldReturnDefaultImageUrlWhenOffline() = runTest {
        // Arrange
        every { mockConnectivityObserver.isCurrentlyConnected } returns false

        // Act
        val result = repository.getImageUrl("LeBron James basketball")

        // Assert
        assertEquals(DEFAULT_IMAGE_URL, result)
    }

    @Test
    fun shouldReturnDefaultImageUrlOnIOException() = runTest {
        // Arrange
        coEvery { mockUnsplashApi.showImageNBA(page = any(), query = any()) } throws IOException("No internet")

        // Act
        val result = repository.getImageUrl("LeBron James basketball")

        // Assert
        assertEquals(DEFAULT_IMAGE_URL, result)
    }

    @Test
    fun shouldReturnDefaultImageUrlOnHttpException() = runTest {
        // Arrange
        val httpException = HttpException(
            Response.error<Any>(403, "Forbidden".toResponseBody(null))
        )
        coEvery { mockUnsplashApi.showImageNBA(page = any(), query = any()) } throws httpException

        // Act
        val result = repository.getImageUrl("LeBron James basketball")

        // Assert
        assertEquals(DEFAULT_IMAGE_URL, result)
    }

    @Test
    fun shouldReturnDefaultImageUrlWhenNoResults() = runTest {
        // Arrange
        val response = UnsplashImageResponse(results = emptyList())
        coEvery { mockUnsplashApi.showImageNBA(page = any(), query = any()) } returns response

        // Act
        val result = repository.getImageUrl("nonexistent query")

        // Assert
        assertEquals(DEFAULT_IMAGE_URL, result)
    }
}
