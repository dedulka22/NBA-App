package com.example.nbaapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nbaapp.data.local.entity.PlayerEntity

@Dao
interface PlayerDao {

    @Query("SELECT * FROM players ORDER BY id ASC")
    fun getPlayers(): PagingSource<Int, PlayerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(players: List<PlayerEntity>)

    @Query("DELETE FROM players")
    suspend fun clearAll()
}
