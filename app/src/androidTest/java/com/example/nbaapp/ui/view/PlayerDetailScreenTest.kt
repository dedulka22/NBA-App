package com.example.nbaapp.ui.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nbaapp.domain.model.PlayerDetail
import com.example.nbaapp.domain.model.Team
import com.example.nbaapp.ui.model.PlayerDetailUiModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun playerDetailContentDisplaysPlayerName() {
        val mockPlayerDetail = PlayerDetail(
            id = 1,
            firstName = "LeBron",
            lastName = "James",
            position = "Forward",
            height = "6'9\"",
            weight = "250 lbs",
            jerseyNumber = "6",
            college = "N/A",
            country = "USA",
            draftYear = 2003,
            draftRound = 1,
            draftNumber = 1,
            team = Team(
                id = 1,
                fullName = "Los Angeles Lakers",
                abbreviation = "LAL",
                city = "Los Angeles",
                conference = "Western",
                division = "Pacific",
                name = "Lakers"
            )
        )

        val mockUiModel = PlayerDetailUiModel(
            playerDetail = mockPlayerDetail,
            imageUrl = ""
        )

        composeTestRule.setContent {
            PlayerDetailContent(
                playerDetailUi = mockUiModel,
                onTeamClick = {}
            )
        }

        composeTestRule.onNodeWithText("LeBron James").assertIsDisplayed()
    }
}
