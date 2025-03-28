package com.example.nbaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaapp.domain.model.Team
import com.example.nbaapp.domain.usecase.GetTeamDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * View model for team details
 * @param getTeamDetailUseCase The use case for getting team details
 */
class TeamDetailViewModel(
    private val getTeamDetailUseCase: GetTeamDetailUseCase
) : ViewModel() {

    private val _teamDetail = MutableStateFlow<Team?>(null)
    val teamDetail: StateFlow<Team?> = _teamDetail.asStateFlow()

    fun loadTeamDetail(teamId: Int) {
        viewModelScope.launch {
            val fetchedTeamDetail = getTeamDetailUseCase(teamId)
            _teamDetail.value = fetchedTeamDetail
        }
    }
}