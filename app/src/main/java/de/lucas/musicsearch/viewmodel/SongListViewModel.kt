package de.lucas.musicsearch.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.musicsearch.model.SongController
import de.lucas.musicsearch.model.api.SongList
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val controller: SongController
) : ViewModel() {
    var chartState by mutableStateOf(true)
    var loadingState by mutableStateOf(LoadingState.DEFAULT)
    var charts by mutableStateOf(SongList(listOf()))

    suspend fun loadChartSongs() {
        charts = controller.loadChartSongs(
            onLoading = { loadingState = LoadingState.LOADING },
            onFinished = { loadingState = LoadingState.DEFAULT },
            onError = { loadingState = LoadingState.ERROR }
        )!!
    }
}

enum class LoadingState {
    DEFAULT,
    LOADING,
    ERROR
}