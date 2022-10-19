package de.lucas.musicsearch.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.musicsearch.model.DatabaseController
import de.lucas.musicsearch.model.SongList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val dbController: DatabaseController) :
    ViewModel() {
    var favoritesList by mutableStateOf(SongList(listOf()))

    fun getFavorites() {
        viewModelScope.launch {
            favoritesList = dbController.getSongListFromDb()
        }
    }
}