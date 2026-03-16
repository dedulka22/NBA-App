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
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nbaapp.R
import com.example.nbaapp.domain.model.Team
import com.example.nbaapp.ui.model.TeamUiModel
import com.example.nbaapp.ui.util.UiState
import com.example.nbaapp.ui.viewmodel.TeamDetailViewModel

/**
 * Composable for displaying team details
 * @param teamDetailViewModel The view model for the team details
 */
@Composable
fun TeamDetailScreenContent(
    teamDetailViewModel: TeamDetailViewModel
) {
    val teamDetailState by teamDetailViewModel.teamDetail.collectAsStateWithLifecycle()

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
                messageResId = state.messageResId,
                formatArgs = state.formatArgs,
                onRetry = { teamDetailViewModel.retry() }
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

@Preview
@Composable
fun TeamDetailContentPreview() {
    TeamDetailContent(
        teamDetailUi = TeamUiModel(
            team = Team(
                id = 1,
                abbreviation = "LAL",
                conference = "Western",
                division = "Pacific",
                city = "Los Angeles",
                name = "Lakers",
                fullName = "Los Angeles Lakers"
            ),
            imageUrl = ""
        )
    )
}
