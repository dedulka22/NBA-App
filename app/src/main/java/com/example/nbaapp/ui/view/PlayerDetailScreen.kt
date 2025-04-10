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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.nbaapp.R
import com.example.nbaapp.domain.model.PlayerDetail
import com.example.nbaapp.ui.viewmodel.PlayerDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Screen for displaying player details
 */
data class PlayerDetailScreen(
    private val playerId: Int
) : Screen {

    override val key: String
        get() = "Player Detail"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val playerDetailViewModel: PlayerDetailViewModel = koinViewModel(
            parameters = { parametersOf(playerId) }
        )

        PlayerDetailScreenContent(
            viewModel = playerDetailViewModel,
            navigator = navigator
        )
    }
}

/**
 * Composable for displaying player details
 * @param viewModel The view model for the player details
 * @param navigator The navigator for the app
 */
@Composable
fun PlayerDetailScreenContent(
    viewModel: PlayerDetailViewModel,
    navigator: Navigator
) {
    val playerDetail by viewModel.playerDetail.collectAsState()

    when (playerDetail) {
        null -> {
            BasketballCircularProgressIndicator()
        }

        else -> {
            playerDetail?.let {
                PlayerDetailContent(it) { teamId ->
                    navigator.push(TeamDetailScreen(teamId))
                }
            }
        }
    }
}

/**
 * Composable for displaying player details
 * @param playerDetail The player details to display
 * @param onTeamClick The callback for when the team is clicked
 */
@Composable
fun PlayerDetailContent(
    playerDetail: PlayerDetail,
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
                GlideImage(playerDetail.image)
                Text(
                    text = "${playerDetail.firstName} ${playerDetail.lastName}",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )

                Text(stringResource(id = R.string.position, playerDetail.position))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.height, playerDetail.height))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.weight, playerDetail.weight))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.jersey, playerDetail.jerseyNumber))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.college, playerDetail.college))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.country, playerDetail.country))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.draft_year, playerDetail.draftYear))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.draft_round, playerDetail.draftRound))
                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(id = R.string.draft_number, playerDetail.draftNumber))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTeamClick(playerDetail.team.id) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.team, playerDetail.team.fullName),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Team Detail",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}
