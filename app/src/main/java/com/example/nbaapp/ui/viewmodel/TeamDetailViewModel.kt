package com.example.nbaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaapp.domain.usecase.GetTeamDetailUseCase
import com.example.nbaapp.ui.model.TeamUiModel
import com.example.nbaapp.ui.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for team detail screen.
 * Manages the state of team detail data with proper loading and error states.
 *
 * @param getTeamDetailUseCase The use case for fetching team details
 * @param teamId The ID of the team to fetch details for
 */
class TeamDetailViewModel(
    private val getTeamDetailUseCase: GetTeamDetailUseCase,
    teamId: Int
) : ViewModel() {

    private val _teamDetail = MutableStateFlow<UiState<TeamUiModel>>(UiState.Initial)
    val teamDetail: StateFlow<UiState<TeamUiModel>> = _teamDetail.asStateFlow()

    init {
        loadTeamDetail(teamId)
    }

    private fun loadTeamDetail(teamId: Int) {
        viewModelScope.launch {
            _teamDetail.value = UiState.Loading
            try {
                val result = getTeamDetailUseCase(teamId)
                _teamDetail.value = UiState.Success(result)
            } catch (e: Exception) {
                _teamDetail.value = UiState.Error(
                    message = e.message ?: "Failed to load team details"
                )
            }
        }
    }

    /**
     * Retry loading team details in case of error.
     */
    fun retry(teamId: Int) {
        loadTeamDetail(teamId)
    }
}