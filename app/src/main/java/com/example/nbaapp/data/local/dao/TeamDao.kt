package com.example.nbaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nbaapp.data.local.entity.TeamEntity

@Dao
interface TeamDao {

    @Query("SELECT * FROM teams WHERE id = :teamId")
    suspend fun getById(teamId: Int): TeamEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(team: TeamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teams: List<TeamEntity>)
}
