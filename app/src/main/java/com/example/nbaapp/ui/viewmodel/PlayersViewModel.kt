package com.example.nbaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.nbaapp.domain.model.Player
import com.example.nbaapp.domain.usecase.GetPlayersUseCase
import kotlinx.coroutines.flow.Flow

/**
 * View model for players
 * @param getPlayersUseCase The use case for getting players
 */
class PlayersViewModel(
    getPlayersUseCase: GetPlayersUseCase
) : ViewModel() {

    val players: Flow<PagingData<Player>> = getPlayersUseCase()
        .cachedIn(viewModelScope)
}