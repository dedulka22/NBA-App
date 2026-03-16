package com.example.nbaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nbaapp.ui.navigation.PlayerDetailRoute
import com.example.nbaapp.ui.navigation.PlayersRoute
import com.example.nbaapp.ui.navigation.TeamDetailRoute
import com.example.nbaapp.ui.theme.NBAAppTheme
import com.example.nbaapp.ui.view.PlayerDetailScreenContent
import com.example.nbaapp.ui.view.PlayersScreenContent
import com.example.nbaapp.ui.view.TeamDetailScreenContent
import com.example.nbaapp.ui.viewmodel.PlayerDetailViewModel
import com.example.nbaapp.ui.viewmodel.PlayersViewModel
import com.example.nbaapp.ui.viewmodel.TeamDetailViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Main activity for the NBA App
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NBAApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NBAApp() {
    NBAAppTheme {
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()

        val currentRoute = currentBackStackEntry?.destination?.route

        val screenTitle = when {
            currentRoute?.contains("PlayerDetailRoute") == true
                -> stringResource(R.string.screen_title_player_detail)
            currentRoute?.contains("TeamDetailRoute") == true
                -> stringResource(R.string.screen_title_team_detail)
            else -> stringResource(R.string.screen_title_players)
        }
        val showBackButton = currentRoute?.contains("PlayersRoute") != true

        Scaffold(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize(),
            topBar = {
                Surface(shadowElevation = 4.dp) {
                    TopAppBar(
                        title = {
                            Row {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.basketball_ball),
                                    contentDescription = stringResource(id = R.string.content_description_basketball_icon),
                                    modifier = Modifier
                                        .size(26.dp)
                                        .padding(end = 8.dp)
                                )
                                Text(screenTitle)
                            }
                        },
                        navigationIcon = {
                            if (showBackButton) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(id = R.string.content_description_back),
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .clickable { navController.popBackStack() },
                                    )
                                }
                            }
                        },
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = PlayersRoute,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable<PlayersRoute> {
                    val viewModel: PlayersViewModel = koinViewModel()
                    PlayersScreenContent(
                        viewModel = viewModel,
                        onPlayerClick = { playerId ->
                            navController.navigate(PlayerDetailRoute(playerId))
                        }
                    )
                }
                composable<PlayerDetailRoute> {
                    val viewModel: PlayerDetailViewModel = koinViewModel()
                    PlayerDetailScreenContent(
                        viewModel = viewModel,
                        onTeamClick = { teamId ->
                            navController.navigate(TeamDetailRoute(teamId))
                        }
                    )
                }
                composable<TeamDetailRoute> {
                    val viewModel: TeamDetailViewModel = koinViewModel()
                    TeamDetailScreenContent(teamDetailViewModel = viewModel)
                }
            }
        }
    }
}
