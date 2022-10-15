package de.lucas.musicsearch.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchedSong(
    @SerialName("tracks")
    val hits: Tracks
) {
    @Serializable
    data class Tracks(
        @SerialName("hits")
        val songList: List<Song>
    ) {
        @Serializable
        data class Song(
            @SerialName("track")
            val track: SongList.Track
        )
    }
}