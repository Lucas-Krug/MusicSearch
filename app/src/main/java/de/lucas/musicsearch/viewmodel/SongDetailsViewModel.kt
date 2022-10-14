package de.lucas.musicsearch.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.musicsearch.model.SongController
import de.lucas.musicsearch.model.SongDetails
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongDetailsViewModel @Inject constructor(private val controller: SongController) :
    ViewModel() {
    private val default = SongDetails(
        key = "",
        title = "N/A",
        subtitle = "N/A",
        images = SongDetails.Images(imageUrl = ""),
        genres = SongDetails.Genre("N/A"),
        sections = listOf()
    )
    var songDetails: SongDetails by mutableStateOf(default)


    fun loadSongDetails(
        key: String,
        onLoading: () -> Unit,
        onFinished: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            songDetails = controller.loadSongDetails(
                key = key,
                onLoading = onLoading,
                onFinished = onFinished,
                onError = onError
            ) ?: default
        }
    }

    fun goToYoutube(url: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.google.android.youtube")
        startActivity(context, intent, null)
    }
}