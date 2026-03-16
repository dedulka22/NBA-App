package com.example.nbaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nbaapp.data.local.entity.PlayerDetailEntity

@Dao
interface PlayerDetailDao {

    @Query("SELECT * FROM player_details WHERE id = :playerId")
    suspend fun getById(playerId: Int): PlayerDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playerDetail: PlayerDetailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(playerDetails: List<PlayerDetailEntity>)
}
