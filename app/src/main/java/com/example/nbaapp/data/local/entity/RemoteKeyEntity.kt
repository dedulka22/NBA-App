package com.example.nbaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val id: String = PLAYERS_KEY,
    val nextCursor: Int?
) {
    companion object {
        const val PLAYERS_KEY = "players_next_cursor"
    }
}
