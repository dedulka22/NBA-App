package com.example.nbaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.nbaapp.domain.model.Player
import com.example.nbaapp.domain.usecase.GetPlayersUseCase
import com.example.nbaapp.domain.util.ConnectivityObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

class PlayersViewModel(
    getPlayersUseCase: GetPlayersUseCase,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    val players: Flow<PagingData<Player>> = getPlayersUseCase()
        .cachedIn(viewModelScope)

    val isConnected: StateFlow<Boolean> = connectivityObserver.isConnected
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT_MS), true)

    companion object {
        private const val SUBSCRIPTION_TIMEOUT_MS = 5000L
    }
}
