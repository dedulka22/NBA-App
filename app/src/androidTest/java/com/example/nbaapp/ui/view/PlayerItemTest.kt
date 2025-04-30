package com.example.nbaapp.ui.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nbaapp.domain.model.Player
import com.example.nbaapp.domain.model.Team
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun playerItemDisplaysPlayerDetails() {
        // Mock player data
        val mockPlayer = Player(
            id = 1,
            firstName = "LeBron",
            lastName = "James",
            position = "Forward",
            team = Team(
                id = 1,
                fullName = "Los Angeles Lakers",
                abbreviation = "LAL",
                city = "Los Angeles",
                conference = "Western",
                division = "Pacific",
                image = "https://example.com/lakers.png",
                name = "Los Angeles Lakers"
            )
        )

        // Set the content of the composable
        composeTestRule.setContent {
            PlayerItem(
                player = mockPlayer,
                onOpenDetails = {}
            )
        }

        // Assert that the player's name, position, and team are displayed
        composeTestRule.onNodeWithText("LeBron James").assertIsDisplayed()
        composeTestRule.onNodeWithText("Position: Forward").assertIsDisplayed()
        composeTestRule.onNodeWithText("Team: LeBron James").assertIsDisplayed()
    }

    @Test
    fun playerItemTriggersOnOpenDetails() {
        // Mock player data
        val mockPlayer = Player(
            id = 1,
            firstName = "LeBron",
            lastName = "James",
            position = "Forward",
            team = Team(
                id = 1,
                fullName = "Los Angeles Lakers",
                abbreviation = "LAL",
                city = "Los Angeles",
                conference = "Western",
                division = "Pacific",
                image = "https://example.com/lakers.png",
                name = "Los Angeles Lakers"
            )
        )

        var wasClicked = false

        // Set the content of the composable
        composeTestRule.setContent {
            PlayerItem(
                player = mockPlayer,
                onOpenDetails = { wasClicked = true }
            )
        }

        // Perform click on the card
        composeTestRule.onNodeWithText("LeBron James").performClick()

        // Assert that the click callback was triggered
        assert(wasClicked)
    }
}