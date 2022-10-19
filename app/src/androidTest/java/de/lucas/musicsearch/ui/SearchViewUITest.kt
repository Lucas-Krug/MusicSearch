package de.lucas.musicsearch.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import de.lucas.musicsearch.view.SearchView
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SearchViewUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun checkTextToSearch() {
        var query = ""
        composeTestRule.setContent {
            SearchView(onClickSearch = { searchText ->
                query = searchText
            })
        }
        composeTestRule.onNodeWithTag("searchIcon").performClick()
        composeTestRule.onNodeWithTag("textField").performTextInput("Hereafter")
        composeTestRule.onNodeWithTag("textField").performImeAction()
        assertEquals("Hereafter", query)
    }
}