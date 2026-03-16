package com.example.nbaapp.domain.util

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val isConnected: Flow<Boolean>
    val isCurrentlyConnected: Boolean
}
