package com.example.nbaapp.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.nbaapp.R
import com.example.nbaapp.ui.viewmodel.PlayersViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Screen for displaying the list of players
 */
object PlayersScreen : Screen {
    private fun readResolve(): Any = PlayersScreen

    override val key: String
        get() = "National Basketball Association"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val playersViewModel: PlayersViewModel = koinViewModel()
        PlayersScreenContent(viewModel = playersViewModel, navigator = navigator)
    }
}

/**
 * Composable for displaying the list of players
 * @param viewModel The view model for the players
 * @param navigator The navigator for the app
 */
@Composable
fun PlayersScreenContent(
    viewModel: PlayersViewModel,
    navigator: Navigator
) {
    val players = viewModel.players.collectAsLazyPagingItems()
    val loadState = players.loadState

    // Handle refresh state (initial loading)
    when (loadState.refresh) {
        is LoadState.Loading -> {
            BasketballCircularProgressIndicator()
            return
        }

        is LoadState.Error -> {
            ErrorScreen(
                message = stringResource(id = R.string.error_loading_players),
                onRetry = { players.retry() }
            )
            return
        }

        is LoadState.NotLoading -> {
            // Continue to show list
        }
    }

    // Show the list
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(players.itemCount) { index ->
            val player = players[index]
            player?.let { item ->
                PlayerItem(
                    player = item,
                    onOpenDetails = {
                        navigator.push(PlayerDetailScreen(item.id))
                    }
                )
            }
        }

        // Handle append state (loading more items)
        when (loadState.append) {
            is LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is LoadState.Error -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(onClick = { players.retry() }) {
                            Text(stringResource(id = R.string.error_loading_players))
                        }
                    }
                }
            }

            is LoadState.NotLoading -> {
                // End of pagination reached or no error
            }
        }
    }
}

