package com.example.nbaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nbaapp.data.local.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getKey(id: String): RemoteKeyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(key: RemoteKeyEntity)

    @Query("DELETE FROM remote_keys")
    suspend fun clearAll()
}
