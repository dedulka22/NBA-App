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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.nbaapp.R
import com.example.nbaapp.domain.util.DataException
import com.example.nbaapp.ui.viewmodel.PlayersViewModel

@Composable
fun PlayersScreenContent(
    viewModel: PlayersViewModel,
    onPlayerClick: (Int) -> Unit
) {
    val players = viewModel.players.collectAsLazyPagingItems()
    val loadState = players.loadState
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()

    // Auto-retry when connectivity is restored
    LaunchedEffect(isConnected) {
        if (isConnected && loadState.refresh is LoadState.Error) {
            players.retry()
        }
    }

    // Handle refresh state (initial loading)
    when (val refresh = loadState.refresh) {
        is LoadState.Loading -> {
            BasketballCircularProgressIndicator()
            return
        }

        is LoadState.Error -> {
            val (messageResId, formatArgs) = errorMessageFor(refresh.error)
            ErrorScreen(
                messageResId = messageResId,
                formatArgs = formatArgs,
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
        items(
            count = players.itemCount,
            key = { index -> players.peek(index)?.id ?: index }
        ) { index ->
            val player = players[index]
            player?.let { item ->
                PlayerItem(
                    player = item,
                    onOpenDetails = {
                        onPlayerClick(item.id)
                    }
                )
            }
        }

        // Handle append state (loading more items)
        when (val append = loadState.append) {
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
                    val (messageResId, formatArgs) = errorMessageFor(append.error)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(onClick = { players.retry() }) {
                            Text(stringResource(id = messageResId, *formatArgs.toTypedArray()))
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

private fun errorMessageFor(error: Throwable): Pair<Int, List<Any>> {
    return when (error) {
        is DataException.Network -> R.string.error_network to emptyList()
        is DataException.Server -> R.string.error_server to listOf(error.code.toString())
        else -> R.string.error_loading_players to emptyList()
    }
}
