package de.lucas.musicsearch.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.musicsearch.model.SongController
import de.lucas.musicsearch.model.api.SongList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val controller: SongController
) : ViewModel() {
    var chartState by mutableStateOf(true)
    var charts by mutableStateOf(SongList(listOf()))

    fun loadChartSongs(
        onLoading: () -> Unit,
        onFinished: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            charts = controller.loadChartSongs(
                onLoading = onLoading,
                onFinished = onFinished,
                onError = onError
            ) ?: SongList(listOf())
        }
    }
}