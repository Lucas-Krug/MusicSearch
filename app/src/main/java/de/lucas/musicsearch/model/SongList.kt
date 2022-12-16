package de.lucas.musicsearch.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SongList(
    @SerialName("tracks")
    val tracks: List<Track>
) {
    @Serializable
    data class Track(
        @SerialName("key")
        val key: String,
        @SerialName("title")
        val title: String,
        @SerialName("subtitle")
        val subtitle: String,
        @SerialName("images")
        val images: Images? = null
    ) {
        @Serializable
        @SerialName("images")
        data class Images(
            @SerialName("coverart")
            val imageUrl: String
        )
    }
}