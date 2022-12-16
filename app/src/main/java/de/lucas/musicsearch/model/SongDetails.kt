package de.lucas.musicsearch.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SongDetails(
    @SerialName("key")
    val key: String,
    @SerialName("title")
    val title: String,
    @SerialName("subtitle")
    val subtitle: String,
    @SerialName("images")
    val images: Images? = null,
    @SerialName("genres")
    val genres: Genre,
    @SerialName("sections")
    val sections: List<Section>
) {
    @Serializable
    @SerialName("images")
    data class Images(
        @SerialName("coverart")
        val imageUrl: String
    )

    @Serializable
    @SerialName("genres")
    data class Genre(
        @SerialName("primary")
        val genre: String
    )

    @Serializable
    @SerialName("sections")
    data class Section(
        @SerialName("youtubeurl")
        val youtubeSection: YoutubeSection? = null,
        @SerialName("metadata")
        val metaData: List<MetaData>? = null
    ) {
        @Serializable
        @SerialName("youtubeurl")
        data class YoutubeSection(
            @SerialName("caption")
            val caption: String,
            @SerialName("image")
            val thumbnail: Thumbnail,
            @SerialName("actions")
            val actions: List<Action>
        ) {
            @Serializable
            @SerialName("image")
            data class Thumbnail(
                @SerialName("url")
                val url: String
            )

            @Serializable
            @SerialName("actions")
            data class Action(
                @SerialName("uri")
                val youtubeUrl: String
            )
        }

        @Serializable
        @SerialName("metadata")
        data class MetaData(
            @SerialName("title")
            val title: String,
            @SerialName("text")
            val text: String
        )
    }
}