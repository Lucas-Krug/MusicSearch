package de.lucas.musicsearch.model

import de.lucas.musicsearch.model.database.SongDao
import de.lucas.musicsearch.model.database.toSongDbItem
import de.lucas.musicsearch.model.database.toSongDetails
import de.lucas.musicsearch.model.database.toTrack
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseController @Inject constructor(private val songDao: SongDao) {

    suspend fun getSongListFromDb(): SongList {
        return SongList(tracks = songDao.getSongList().map { song ->
            song.toTrack()
        })
    }

    suspend fun getSongDetails(key: String): SongDetails =
        songDao.getSongDetails(key).toSongDetails()

    suspend fun setSongAsFavorite(song: SongDetails) = songDao.addSong(song.toSongDbItem())

    suspend fun removeSongFromFavorite(key: String) = songDao.removeSong(key)

    suspend fun isSongFavorite(key: String) = songDao.isSongFavorite(key)
}