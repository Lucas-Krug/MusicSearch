package de.lucas.musicsearch.net

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import de.lucas.musicsearch.model.SongDetails
import de.lucas.musicsearch.model.SongDetails.*
import de.lucas.musicsearch.model.SongDetails.Section.MetaData
import de.lucas.musicsearch.model.SongDetails.Section.YoutubeSection
import de.lucas.musicsearch.model.SongDetails.Section.YoutubeSection.Action
import de.lucas.musicsearch.model.SongDetails.Section.YoutubeSection.Thumbnail
import de.lucas.musicsearch.model.SongList
import de.lucas.musicsearch.model.SongList.Track
import de.lucas.musicsearch.model.api.ApiService
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.SocketException

class ApiTest {

    private val server: MockWebServer = MockWebServer()

    private lateinit var api: ApiService

    private val jsonFileSongList =
        File("src/test/java/de/lucas/musicsearch/resources/songlist_success_response.json")

    private val jsonFileSongDetails =
        File("src/test/java/de/lucas/musicsearch/resources/songdetails_success_response.json")

    @OptIn(ExperimentalSerializationApi::class)
    @Before
    fun setUp() {
        server.start(8080)

        api = Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(server.url("/"))
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun readJsonFile() {
        val reader = MockResponseFileReader(jsonFileSongList)
        Assert.assertNotNull(reader)
    }

    @Test
    fun testSuccessfulConnectionForSongList() = runBlocking {
        val format = Json { ignoreUnknownKeys = true }
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader(jsonFileSongList).content)
        )
        val response = api.fetchTop20SongsFromServer("charts/track", "")
        val body = format.decodeFromString<SongList>(response.body()!!.string())

        val expected = SongList(
            listOf(
                Track(
                    key = "590865488",
                    title = "Yours",
                    subtitle = "JIN",
                    images = Track.Images(
                        imageUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/45/ce/0d/45ce0d71-a400-c4d4-253b-299eaf34eb5e/8809829712307.jpg/400x400cc.jpg"
                    )
                ),
                Track(
                    key = "498502624",
                    title = "Under The Influence",
                    subtitle = "Chris Brown",
                    images = Track.Images(
                        imageUrl = "https://is5-ssl.mzstatic.com/image/thumb/Music122/v4/97/ec/96/97ec963b-8829-f040-fe40-508069d6044b/196589418449.jpg/400x400cc.jpg"
                    )
                )
            )
        )
        Assert.assertEquals(200, response.code())
        Assert.assertEquals(expected, body)
    }

    @Test
    fun testSuccessfulConnectionForSongDetails() = runBlocking {
        val format = Json { ignoreUnknownKeys = true }
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader(jsonFileSongDetails).content)
        )
        val response =
            api.fetchSongDetailsFromServer("songs/get-details?key=40333609&locale=en-US", "")
        val body = format.decodeFromString<SongDetails>(response.body()!!.string())
        Timber.e(body.toString())
        val expected = SongDetails(
            key = "40333609",
            title = "River Flows In You",
            subtitle = "Yiruma",
            images = Images(imageUrl = "https://is2-ssl.mzstatic.com/image/thumb/Music115/v4/5f/f4/9b/5ff49b8c-d0bb-3748-14f4-131edfb332ce/first_love_3000.jpg/400x400cc.jpg"),
            genres = Genre(genre = "New Age"),
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
        Assert.assertEquals(200, response.code())
        Assert.assertEquals(expected, body)
    }

    @Test
    fun noInternetConnection() = runBlocking {
        val response = MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        var call: Response<ResponseBody>? = null
        server.enqueue(response)

        try {
            call = api.fetchTop20SongsFromServer("charts/track", "")
        } catch (e: SocketException) {
            println("No Internet Connection")
            assert(call == null)
        }
    }

    class MockResponseFileReader(file: File) {
        val content: String

        init {
            val bytes = ByteArray(file.length().toInt())

            val inSteam = FileInputStream(file)
            inSteam.use { steam ->
                steam.read(bytes)
            }

            content = String(bytes)
            Timber.e(content)
        }
    }
}