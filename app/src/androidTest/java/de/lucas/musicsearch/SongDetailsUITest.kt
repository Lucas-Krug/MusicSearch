package de.lucas.musicsearch

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.lucas.musicsearch.model.SongDetails
import de.lucas.musicsearch.view.SongDetailsScreen
import de.lucas.musicsearch.viewmodel.SongDetailsViewModel
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SongDetailsUITest {

    private val songDetailsViewModel = SongDetailsViewModel(mockk())

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        composeTestRule.setContent {
            SongDetailsScreen(
                SongDetails(
                    key = "",
                    title = "Title",
                    subtitle = "Artist",
                    images = SongDetails.Images(imageUrl = ""),
                    genres = SongDetails.Genre("N/A"),
                    sections = listOf(
                        SongDetails.Section(
                            metaData = null,
                            youtubeSection = SongDetails.Section.YoutubeSection(
                                caption = "Yiruma, (이루마) - River Flows in You",
                                thumbnail = SongDetails.Section.YoutubeSection.Thumbnail("https://i.ytimg.com/vi/7maJOI3QMu0/maxresdefault.jpg"),
                                actions = listOf(
                                    SongDetails.Section.YoutubeSection.Action(
                                        youtubeUrl = "https://youtu.be/7maJOI3QMu0?autoplay=1"
                                    )
                                )
                            )
                        )
                    ),
                ),
                goToYoutube = { youtubeUrl ->
                    songDetailsViewModel.goToYoutube(url = youtubeUrl, context = mockk())
                }
            )
        }
    }

    @Test
    fun validateGoToYoutubeIntent() {
        composeTestRule.onNodeWithTag("youtubeImage").assertExists()
        intended(toPackage("com.google.android.youtube"))
    }
}