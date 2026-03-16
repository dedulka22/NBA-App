package com.example.nbaapp.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaapp.R
import com.example.nbaapp.domain.usecase.GetPlayerDetailUseCase
import com.example.nbaapp.domain.util.ConnectivityObserver
import com.example.nbaapp.domain.util.DataException
import com.example.nbaapp.ui.model.PlayerDetailUiModel
import com.example.nbaapp.ui.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class PlayerDetailViewModel(
    private val getPlayerDetailUseCase: GetPlayerDetailUseCase,
    savedStateHandle: SavedStateHandle,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val playerId: Int = checkNotNull(savedStateHandle[KEY_PLAYER_ID])

    private val _playerDetail = MutableStateFlow<UiState<PlayerDetailUiModel>>(UiState.Initial)
    val playerDetail: StateFlow<UiState<PlayerDetailUiModel>> = _playerDetail.asStateFlow()

    init {
        loadPlayerDetail(playerId)
        observeConnectivity(connectivityObserver)
    }

    private fun observeConnectivity(connectivityObserver: ConnectivityObserver) {
        viewModelScope.launch {
            connectivityObserver.isConnected
                .distinctUntilChanged()
                .drop(1)
                .filter { it }
                .collect {
                    if (_playerDetail.value is UiState.Error) {
                        loadPlayerDetail(playerId)
                    }
                }
        }
    }

    private fun loadPlayerDetail(playerId: Int) {
        viewModelScope.launch {
            _playerDetail.value = UiState.Loading
            try {
                val (playerDetail, imageUrl) = getPlayerDetailUseCase(playerId)
                _playerDetail.value = UiState.Success(
                    PlayerDetailUiModel(playerDetail = playerDetail, imageUrl = imageUrl)
                )
            } catch (e: DataException.Network) {
                _playerDetail.value = UiState.Error(
                    messageResId = R.string.error_network
                )
            } catch (e: DataException.Server) {
                _playerDetail.value = UiState.Error(
                    messageResId = R.string.error_server,
                    formatArgs = listOf(e.code.toString())
                )
            }
        }
    }

    fun retry() {
        loadPlayerDetail(playerId)
    }

    companion object {
        const val KEY_PLAYER_ID = "playerId"
    }
}
