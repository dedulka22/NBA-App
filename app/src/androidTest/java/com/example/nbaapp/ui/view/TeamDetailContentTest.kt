package com.example.nbaapp.ui.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nbaapp.domain.model.Team
import com.example.nbaapp.ui.model.TeamUiModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TeamDetailContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun teamDetailContentDisplaysTeamDetails() {
        val mockTeamUiModel = TeamUiModel(
            team = Team(
                id = 1,
                fullName = "Los Angeles Lakers",
                abbreviation = "LAL",
                city = "Los Angeles",
                conference = "Western",
                division = "Pacific",
                name = "Lakers"
            ),
            imageUrl = ""
        )

        composeTestRule.setContent {
            TeamDetailContent(teamDetailUi = mockTeamUiModel)
        }

        composeTestRule.onNodeWithText("Los Angeles Lakers").assertIsDisplayed()
        composeTestRule.onNodeWithText("Abbreviation: LAL").assertIsDisplayed()
        composeTestRule.onNodeWithText("City: Los Angeles").assertIsDisplayed()
        composeTestRule.onNodeWithText("Conference: Western").assertIsDisplayed()
        composeTestRule.onNodeWithText("Division: Pacific").assertIsDisplayed()
    }
}
