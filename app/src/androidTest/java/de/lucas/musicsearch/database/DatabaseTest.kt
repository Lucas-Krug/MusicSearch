package de.lucas.musicsearch.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.lucas.musicsearch.model.SongDetails
import de.lucas.musicsearch.model.SongDetails.Section
import de.lucas.musicsearch.model.SongDetails.Section.MetaData
import de.lucas.musicsearch.model.SongDetails.Section.YoutubeSection
import de.lucas.musicsearch.model.SongDetails.Section.YoutubeSection.Action
import de.lucas.musicsearch.model.SongDetails.Section.YoutubeSection.Thumbnail
import de.lucas.musicsearch.model.database.SongDao
import de.lucas.musicsearch.model.database.SongDb
import de.lucas.musicsearch.model.database.toSongDbItem
import de.lucas.musicsearch.model.database.toSongDetails
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var songDao: SongDao
    private lateinit var db: SongDb

    private val song = SongDetails(
        key = "40333609",
        title = "River Flows In You",
        subtitle = "Yiruma",
        images = SongDetails.Images(imageUrl = "https://is2-ssl.mzstatic.com/image/thumb/Music115/v4/5f/f4/9b/5ff49b8c-d0bb-3748-14f4-131edfb332ce/first_love_3000.jpg/400x400cc.jpg"),
        genres = SongDetails.Genre(genre = "New Age"),
        sections = listOf(
            Section(
                youtubeSection = null,
                metaData = listOf(
                    MetaData(
                        title = "Album",
                        text = "Yiruma 2nd Album 'First Love' (The Original & the Very First Recording)"
                    ),
                    MetaData(
                        title = "Label",
                        text = "Stomp Music"
                    ),
                    MetaData(
                        title = "Released",
                        text = "2001"
                    )
                )
            ),
            Section(
                metaData = null,
                youtubeSection = YoutubeSection(
                    caption = "Yiruma, (이루마) - River Flows in You",
                    thumbnail = Thumbnail("https://i.ytimg.com/vi/7maJOI3QMu0/maxresdefault.jpg"),
                    actions = listOf(Action(youtubeUrl = "https://youtu.be/7maJOI3QMu0?autoplay=1"))
                )
            ),
            Section(metaData = null, youtubeSection = null)
        )
    )

    @Before
    fun setUp() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, SongDb::class.java
        ).build()
        songDao = db.songDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertSongIntoDb() = runBlocking {
        songDao.addSong(song.toSongDbItem())
        assertEquals(song.key, songDao.getSongDetails(key = "40333609").toSongDetails().key)
    }

    @Test
    fun isSongInDb() = runBlocking {
        songDao.addSong(song.toSongDbItem())
        assertEquals(true, songDao.isSongFavorite(key = "40333609"))
    }

    @Test
    fun removeSongFromDb() = runBlocking {
        songDao.addSong(song.toSongDbItem())
        assertEquals(true, songDao.isSongFavorite("40333609"))
        songDao.removeSong("40333609")
        assertEquals(false, songDao.isSongFavorite("40333609"))
    }
}