package de.lucas.musicsearch.net

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import de.lucas.musicsearch.model.api.ApiService
import de.lucas.musicsearch.model.api.SongList
import de.lucas.musicsearch.model.api.SongList.Track
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
import retrofit2.*
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.SocketException

class ApiTest {

    private val server: MockWebServer = MockWebServer()

    private lateinit var api: ApiService

    private val jsonFile =
        File("src/test/java/de/lucas/musicsearch/resources/success_response.json")

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
        val reader = MockResponseFileReader(jsonFile)
        Assert.assertNotNull(reader)
    }

    @Test
    fun testSuccessfulConnection() = runBlocking {
        val format = Json { ignoreUnknownKeys = true }
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader(jsonFile).content)
        )
        val response =
            format.decodeFromString<SongList>(api.fetchSongsFromServer("charts/track").string())

        val expected = SongList(
            listOf(
                Track(
                    key = "590865488",
                    title = "Yours",
                    subtitle = "JIN",
                    images = Track.Images(
                        image = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/45/ce/0d/45ce0d71-a400-c4d4-253b-299eaf34eb5e/8809829712307.jpg/400x400cc.jpg"
                    )
                ),
                Track(
                    key = "498502624",
                    title = "Under The Influence",
                    subtitle = "Chris Brown",
                    images = Track.Images(
                        image = "https://is5-ssl.mzstatic.com/image/thumb/Music122/v4/97/ec/96/97ec963b-8829-f040-fe40-508069d6044b/196589418449.jpg/400x400cc.jpg"
                    )
                )
            )
        )
        Assert.assertEquals(expected, response)
    }

    @Test
    fun noInternetConnection() = runBlocking {
        val response = MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        var call: ResponseBody? = null
        server.enqueue(response)

        try {
            call = api.fetchSongsFromServer("charts/track")
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