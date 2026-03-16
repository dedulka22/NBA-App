package com.example.nbaapp.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaapp.R
import com.example.nbaapp.domain.usecase.GetTeamDetailUseCase
import com.example.nbaapp.domain.util.ConnectivityObserver
import com.example.nbaapp.domain.util.DataException
import com.example.nbaapp.ui.model.TeamUiModel
import com.example.nbaapp.ui.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class TeamDetailViewModel(
    private val getTeamDetailUseCase: GetTeamDetailUseCase,
    savedStateHandle: SavedStateHandle,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val teamId: Int = checkNotNull(savedStateHandle[KEY_TEAM_ID])

    private val _teamDetail = MutableStateFlow<UiState<TeamUiModel>>(UiState.Initial)
    val teamDetail: StateFlow<UiState<TeamUiModel>> = _teamDetail.asStateFlow()

    init {
        loadTeamDetail(teamId)
        observeConnectivity(connectivityObserver)
    }

    private fun observeConnectivity(connectivityObserver: ConnectivityObserver) {
        viewModelScope.launch {
            connectivityObserver.isConnected
                .distinctUntilChanged()
                .drop(1)
                .filter { it }
                .collect {
                    if (_teamDetail.value is UiState.Error) {
                        loadTeamDetail(teamId)
                    }
                }
        }
    }

    private fun loadTeamDetail(teamId: Int) {
        viewModelScope.launch {
            _teamDetail.value = UiState.Loading
            try {
                val (team, imageUrl) = getTeamDetailUseCase(teamId)
                _teamDetail.value = UiState.Success(
                    TeamUiModel(team = team, imageUrl = imageUrl)
                )
            } catch (e: DataException.Network) {
                _teamDetail.value = UiState.Error(
                    messageResId = R.string.error_network
                )
            } catch (e: DataException.Server) {
                _teamDetail.value = UiState.Error(
                    messageResId = R.string.error_server,
                    formatArgs = listOf(e.code.toString())
                )
            }
        }
    }

    fun retry() {
        loadTeamDetail(teamId)
    }

    companion object {
        const val KEY_TEAM_ID = "teamId"
    }
}
