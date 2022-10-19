package de.lucas.musicsearch.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import de.lucas.musicsearch.model.SongDbItem

@Database(entities = [SongDbItem::class], version = 1, exportSchema = false)
abstract class SongDb : RoomDatabase() {

    abstract fun songDao(): SongDao
}