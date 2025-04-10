package com.example.nbaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaapp.domain.model.PlayerDetail
import com.example.nbaapp.domain.usecase.GetPlayerDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * View model for player details
 * @param getPlayerDetailUseCase The use case for getting player details
 */
class PlayerDetailViewModel(
    private val getPlayerDetailUseCase: GetPlayerDetailUseCase,
    playerId: Int
) : ViewModel() {

    private val _playerDetail = MutableStateFlow<PlayerDetail?>(null)
    val playerDetail: StateFlow<PlayerDetail?> = _playerDetail.asStateFlow()

    init {
        loadPlayerDetail(playerId)
    }

    private fun loadPlayerDetail(playerId: Int) {
        viewModelScope.launch {
            val fetchedPlayerDetail = getPlayerDetailUseCase(playerId)
            _playerDetail.value = fetchedPlayerDetail
        }
    }
}