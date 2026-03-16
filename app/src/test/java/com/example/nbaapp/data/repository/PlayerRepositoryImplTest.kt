package com.example.nbaapp.data.repository

import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.local.NBADatabase
import com.example.nbaapp.data.local.dao.PlayerDao
import com.example.nbaapp.data.local.dao.RemoteKeyDao
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class PlayerRepositoryImplTest {

    private lateinit var repository: PlayerRepositoryImpl
    private lateinit var mockApi: NBAApi
    private lateinit var mockDatabase: NBADatabase

    @Before
    fun setUp() {
        mockApi = mockk()
        mockDatabase = mockk()
        every { mockDatabase.playerDao() } returns mockk<PlayerDao>()
        every { mockDatabase.remoteKeyDao() } returns mockk<RemoteKeyDao>()
        repository = PlayerRepositoryImpl(mockApi, mockDatabase)
    }

    @Test
    fun getPlayersShouldReturnNonNullFlow() {
        // Act
        val result = repository.getPlayers()

        // Assert
        assertNotNull(result)
    }
}
