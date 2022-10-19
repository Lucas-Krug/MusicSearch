package de.lucas.musicsearch.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import de.lucas.musicsearch.model.SongList

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoriteScreen(
    songs: SongList,
    onClickSong: (String) -> Unit
) {
    Column {
        if (songs.tracks.isEmpty()) {
            NoFavoritesScreen()
        }
        SongList(
            songs = songs,
            onClickSong = { key -> onClickSong(key) })
    }
}