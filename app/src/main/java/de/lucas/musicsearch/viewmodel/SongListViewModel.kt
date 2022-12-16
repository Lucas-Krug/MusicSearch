package de.lucas.musicsearch.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.musicsearch.model.SearchedSong
import de.lucas.musicsearch.model.SongController
import de.lucas.musicsearch.model.SongList
import de.lucas.musicsearch.model.SongList.Track.Images
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val controller: SongController
) : ViewModel() {
    var chartState by mutableStateOf(true)
    var songList by mutableStateOf(SongList(listOf()))

    fun loadChartSongs(
        onLoading: () -> Unit,
        onFinished: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            songList = controller.loadChartSongs(
                onLoading = onLoading,
                onFinished = onFinished,
                onError = onError
            ) ?: SongList(listOf())
            Timber.e(songList.toString())
        }
    }

    fun loadSearchedSong(
        searchedText: String,
        onLoading: () -> Unit,
        onFinished: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            val searchedSongs = controller.loadSearchedSongs(
                searchedText = searchedText,
                onLoading = onLoading,
                onFinished = onFinished,
                onError = onError
            ) ?: SearchedSong(SearchedSong.Tracks(listOf()))
            songList = parseSearchedSongToSongList(searchedSongs)
        }
    }

    private fun parseSearchedSongToSongList(searchedSongs: SearchedSong): SongList {
        val tracks = searchedSongs.hits.songList.map { song ->
            SongList.Track(
                key = song.track.key,
                title = song.track.title,
                subtitle = song.track.subtitle,
                images = Images(imageUrl = song.track.images?.imageUrl ?: "")
            )
        }
        return SongList(tracks)
    }
}