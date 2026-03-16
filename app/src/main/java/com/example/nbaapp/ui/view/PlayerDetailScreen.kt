package com.example.nbaapp.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.nbaapp.R
import com.example.nbaapp.ui.model.PlayerDetailUiModel
import com.example.nbaapp.ui.util.UiState
import com.example.nbaapp.ui.viewmodel.PlayerDetailViewModel

/**
 * Composable for displaying player details
 * @param viewModel The view model for the player details
 * @param onTeamClick Callback when the team is clicked
 */
@Composable
fun PlayerDetailScreenContent(
    viewModel: PlayerDetailViewModel,
    onTeamClick: (Int) -> Unit
) {
    val playerDetailState by viewModel.playerDetail.collectAsStateWithLifecycle()

    when (val state = playerDetailState) {
        is UiState.Initial,
        is UiState.Loading -> {
            BasketballCircularProgressIndicator()
        }

        is UiState.Success -> {
            PlayerDetailContent(state.data, onTeamClick)
        }

        is UiState.Error -> {
            ErrorScreen(
                messageResId = state.messageResId,
                formatArgs = state.formatArgs,
                onRetry = { viewModel.retry() }
            )
        }
    }
}

/**
 * Composable for displaying player details
 * @param playerDetailUi The player details UI model to display
 * @param onTeamClick The callback for when the team is clicked
 */
@Composable
fun PlayerDetailContent(
    playerDetailUi: PlayerDetailUiModel,
    onTeamClick: (Int) -> Unit
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
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                GlideImage(playerDetailUi.imageUrl)
                Text(
                    text = playerDetailUi.fullName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )

                Text(stringResource(id = R.string.position, playerDetailUi.position))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.height, playerDetailUi.height))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.weight, playerDetailUi.weight))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.jersey, playerDetailUi.jerseyNumber))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.college, playerDetailUi.college))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.country, playerDetailUi.country))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.draft_year, playerDetailUi.draftYear))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.draft_round, playerDetailUi.draftRound))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.draft_number, playerDetailUi.draftNumber))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTeamClick(playerDetailUi.team.id) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.team, playerDetailUi.team.fullName),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(id = R.string.content_description_team_detail),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}
