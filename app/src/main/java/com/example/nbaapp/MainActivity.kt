package com.example.nbaapp

import android.annotation.SuppressLint
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.example.nbaapp.ui.theme.NBAAppTheme
import com.example.nbaapp.ui.view.PlayersScreen

/**
 * Main activity for the NBA App
 */
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NBAApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NBAApp() {
    NBAAppTheme {
        Navigator(PlayersScreen) { navigator ->
            val currentScreen = navigator.items.lastOrNull() ?: PlayersScreen

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
                                        contentDescription = "Basketball Icon",
                                        modifier = Modifier
                                            .size(26.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Text(currentScreen.key)
                                }
                            },
                            navigationIcon = {
                                if (currentScreen !is PlayersScreen) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back",
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .clickable { navigator.pop() },
                                        )
                                    }
                                }
                            },
                        )
                    }
                }
            ) { paddingValues ->
                Column(modifier = Modifier.padding(paddingValues)) {
                    CurrentScreen()
                }
            }
        }
    }
}