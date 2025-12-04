package com.example.nbaapp.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.example.nbaapp.R
import com.example.nbaapp.domain.model.Team
import com.example.nbaapp.ui.model.TeamUiModel
import com.example.nbaapp.ui.util.UiState
import com.example.nbaapp.ui.viewmodel.TeamDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Screen for displaying team details
 */
class TeamDetailScreen(
    private val teamId: Int
) : Screen {

    override val key: String
        get() = "Team Detail"

    @Composable
    override fun Content() {
        val teamDetailViewModel: TeamDetailViewModel = koinViewModel(
            parameters = { parametersOf(teamId) }
        )

        TeamDetailScreenContent(
            teamDetailViewModel = teamDetailViewModel
        )
    }
}

/**
 * Composable for displaying team details
 * @param teamDetailViewModel The view model for the team details
 */
@Composable
fun TeamDetailScreenContent(
    teamDetailViewModel: TeamDetailViewModel
) {
    val teamDetailState by teamDetailViewModel.teamDetail.collectAsState()

    when (val state = teamDetailState) {
        is UiState.Initial,
        is UiState.Loading -> {
            BasketballCircularProgressIndicator()
        }

        is UiState.Success -> {
            TeamDetailContent(state.data)
        }

        is UiState.Error -> {
            ErrorScreen(
                message = state.message,
                onRetry = { /* viewModel.retry() - would need teamId */ }
            )
        }
    }
}

@Composable
fun TeamDetailContent(
    teamDetailUi: TeamUiModel
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                GlideImage(teamDetailUi.imageUrl)
                Text(
                    text = teamDetailUi.fullName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )

                Text(stringResource(id = R.string.abbreviation, teamDetailUi.abbreviation))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.city, teamDetailUi.city))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.conference, teamDetailUi.conference))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.division, teamDetailUi.division))
            }
        }
    }
}
