package de.lucas.musicsearch.model.database

import de.lucas.musicsearch.model.SongDbItem
import de.lucas.musicsearch.model.SongDetails
import de.lucas.musicsearch.model.SongDetails.*
import de.lucas.musicsearch.model.SongDetails.Section.MetaData
import de.lucas.musicsearch.model.SongDetails.Section.YoutubeSection
import de.lucas.musicsearch.model.SongDetails.Section.YoutubeSection.Action
import de.lucas.musicsearch.model.SongDetails.Section.YoutubeSection.Thumbnail
import de.lucas.musicsearch.model.SongList.Track

fun SongDetails.toSongDbItem(): SongDbItem {
    var youtubeCaption = ""
    var youtubeThumbnail = ""
    var youtubeUrl = ""
    var album = ""
    var label = ""
    var released = ""
    this.sections.forEach { section ->
        if (section.metaData != null) {
            album = section.metaData[0].text
            label = section.metaData[1].text
            released = section.metaData[2].text
        }
        if (section.youtubeSection != null) {
            youtubeCaption = section.youtubeSection.caption
            youtubeThumbnail = section.youtubeSection.thumbnail.url
            youtubeUrl = section.youtubeSection.actions[0].youtubeUrl
        }
    }
    return SongDbItem(
        key = this.key,
        title = this.title,
        artist = this.subtitle,
        imageUrl = this.images?.imageUrl ?: "",
        genre = this.genres.genre,
        youtubeCaption = youtubeCaption,
        youtubeThumbnail = youtubeThumbnail,
        youtubeUrl = youtubeUrl,
        album = album,
        label = label,
        released = released
    )
}

fun SongDbItem.toSongDetails() = SongDetails(
    key = this.key,
    title = this.title,
    subtitle = this.artist,
    images = Images(imageUrl = this.imageUrl),
    genres = Genre(this.genre),
    sections = listOf(
        Section(
            metaData = null,
            youtubeSection = YoutubeSection(
                caption = this.youtubeCaption,
                thumbnail = Thumbnail(this.youtubeThumbnail),
                actions = listOf(
                    Action(
                        youtubeUrl = this.youtubeUrl
                    )
                )
            )
        ),
        Section(
            metaData = listOf(
                MetaData(title = "album", text = this.album),
                MetaData(title = "label", text = this.label),
                MetaData(title = "released", text = this.released)
            ),
            youtubeSection = null
        )
    )
)

fun SongDbItem.toTrack() = Track(
    key = this.key,
    title = this.title,
    subtitle = this.artist,
    images = Track.Images(imageUrl = this.imageUrl)
)