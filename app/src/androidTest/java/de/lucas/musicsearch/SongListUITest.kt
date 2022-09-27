package de.lucas.musicsearch

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.lucas.musicsearch.view.SongListScreen
import de.lucas.musicsearch.viewmodel.RootViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SongListUITest {

    private lateinit var rootViewModel: RootViewModel

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_top20_Button() {
        composeTestRule.setContent {
            rootViewModel = RootViewModel()
            SongListScreen(rootViewModel.chartState) {
                rootViewModel.chartState = !rootViewModel.chartState
            }
        }
        composeTestRule.onNodeWithText("Top 20").performClick()
        assertEquals(false, rootViewModel.chartState)
    }
}