package com.example.nbaapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nbaapp.data.local.dao.PlayerDao
import com.example.nbaapp.data.local.dao.PlayerDetailDao
import com.example.nbaapp.data.local.dao.RemoteKeyDao
import com.example.nbaapp.data.local.dao.TeamDao
import com.example.nbaapp.data.local.entity.PlayerDetailEntity
import com.example.nbaapp.data.local.entity.PlayerEntity
import com.example.nbaapp.data.local.entity.RemoteKeyEntity
import com.example.nbaapp.data.local.entity.TeamEntity

@Database(
    entities = [
        PlayerEntity::class,
        PlayerDetailEntity::class,
        TeamEntity::class,
        RemoteKeyEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NBADatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun playerDetailDao(): PlayerDetailDao
    abstract fun teamDao(): TeamDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
