package com.example.nbaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaapp.domain.usecase.GetPlayerDetailUseCase
import com.example.nbaapp.ui.model.PlayerDetailUiModel
import com.example.nbaapp.ui.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for player detail screen.
 * Manages the state of player detail data with proper loading and error states.
 *
 * @param getPlayerDetailUseCase The use case for fetching player details
 * @param playerId The ID of the player to fetch details for
 */
class PlayerDetailViewModel(
    private val getPlayerDetailUseCase: GetPlayerDetailUseCase,
    playerId: Int
) : ViewModel() {

    private val _playerDetail = MutableStateFlow<UiState<PlayerDetailUiModel>>(UiState.Initial)
    val playerDetail: StateFlow<UiState<PlayerDetailUiModel>> = _playerDetail.asStateFlow()

    init {
        loadPlayerDetail(playerId)
    }

    private fun loadPlayerDetail(playerId: Int) {
        viewModelScope.launch {
            _playerDetail.value = UiState.Loading
            try {
                val result = getPlayerDetailUseCase(playerId)
                _playerDetail.value = UiState.Success(result)
            } catch (e: Exception) {
                _playerDetail.value = UiState.Error(
                    message = e.message ?: "Failed to load player details"
                )
            }
        }
    }

    /**
     * Retry loading player details in case of error.
     */
    fun retry(playerId: Int) {
        loadPlayerDetail(playerId)
    }
}