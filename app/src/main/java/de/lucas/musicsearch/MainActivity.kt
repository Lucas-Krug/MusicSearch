package de.lucas.musicsearch

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import de.lucas.musicsearch.view.Root
import de.lucas.musicsearch.view.theme.MusicSearchTheme
import de.lucas.musicsearch.view.theme.White

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setStatusBarColor(White)
            }
            MusicSearchTheme {
                Root()
            }
        }
    }
}