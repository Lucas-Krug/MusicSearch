package de.lucas.musicsearch.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.lucas.musicsearch.view.SongListScreen
import de.lucas.musicsearch.viewmodel.LoadingState
import de.lucas.musicsearch.viewmodel.SongListViewModel
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SongListUITest {

    private var viewModel = SongListViewModel(mockk())
    private var key = ""

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            SongListScreen(
                de.lucas.musicsearch.model.SongList(
                    listOf(
                        de.lucas.musicsearch.model.SongList.Track(
                            key = "1234",
                            title = "Title",
                            subtitle = "Artist",
                            images = de.lucas.musicsearch.model.SongList.Track.Images(imageUrl = "")
                        )
                    )
                ), false, LoadingState.DEFAULT, {
                    if (!viewModel.chartState) {
                        viewModel.chartState = true
                    }
                }) { id ->
                key = id
            }
        }
    }

    @Test
    fun test_top20_Button() {
        viewModel.chartState = false
        composeTestRule.onNodeWithText("Top 20").performClick()
        assertEquals(true, viewModel.chartState)
    }

    @Test
    fun test_onClick_SongItem() {
        composeTestRule.onNodeWithTag("songList").onChildren().onFirst().performClick()
        assertEquals("1234", key)
    }
}