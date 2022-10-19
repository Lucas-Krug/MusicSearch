package de.lucas.musicsearch.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.lucas.musicsearch.model.SongDbItem

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(song: SongDbItem)

    @Query("SELECT * FROM song_table")
    suspend fun getSongList(): List<SongDbItem>

    @Query("SELECT * FROM song_table WHERE `key` = :key")
    suspend fun getSongDetails(key: String): SongDbItem

    @Query("SELECT EXISTS(SELECT * FROM song_table WHERE `key` = :key)")
    suspend fun isSongFavorite(key: String): Boolean

    @Query("DELETE FROM song_table WHERE `key` = :key")
    suspend fun removeSong(key: String)
}