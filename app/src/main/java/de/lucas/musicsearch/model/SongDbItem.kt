package de.lucas.musicsearch.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* data class to save song in database
*/
@Entity(tableName = "song_table")
data class SongDbItem(
    @PrimaryKey
    val key: String,
    val title: String,
    val artist: String,
    val imageUrl: String,
    val genre: String,
    val youtubeCaption: String,
    val youtubeThumbnail: String,
    var youtubeUrl: String,
    val album: String,
    val label: String,
    val released: String
)