package com.example.nbaapp.ui.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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

    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {

        items(players.itemCount) { index ->
            val player = players[index]
            player?.let { item ->
                PlayerItem(player = item, onOpenDetails = {
                    navigator.push(PlayerDetailScreen(item.id))
                })
            }
        }

        when (loadState.append) {
            is LoadState.Loading -> {
                item {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }
            }

            is LoadState.Error -> {
                item {
                    Text(stringResource(id = R.string.error_loading_players))
                }
            }

            is LoadState.NotLoading -> {
                if (!loadState.append.endOfPaginationReached) {
                    players.retry()
                }
            }
        }
    }

    LaunchedEffect(remember { derivedStateOf { listState.firstVisibleItemIndex } }) {
        if (!loadState.append.endOfPaginationReached) {
            players.retry()
        }
    }
}

