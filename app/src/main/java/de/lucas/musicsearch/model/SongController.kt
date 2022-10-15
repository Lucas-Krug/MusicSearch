package de.lucas.musicsearch.model

import de.lucas.musicsearch.model.api.ApiClient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongController @Inject constructor(
    private val apiClient: ApiClient
) {

    suspend fun loadChartSongs(
        onLoading: () -> Unit,
        onFinished: () -> Unit,
        onError: () -> Unit
    ): SongList? {
        onLoading()
        val format = Json { ignoreUnknownKeys = true }

        return try {
            val response = apiClient.fetchTop20SongsFromServer()

            if (response.code() == 200) {
                onFinished()
                return format.decodeFromString(response.body()!!.string())
            } else {
                onError()
                null
            }
        } catch (e: Exception) {
            onError()
            null
        }
    }

    suspend fun loadSongDetails(
        key: String,
        onLoading: () -> Unit,
        onFinished: () -> Unit,
        onError: () -> Unit
    ): SongDetails? {
        onLoading()
        val format = Json { ignoreUnknownKeys = true }

        return try {
            val response = apiClient.fetchSongDetailsFromServer(key)

            if (response.code() == 200) {
                onFinished()
                return format.decodeFromString(response.body()!!.string())
            } else {
                onError()
                null
            }
        } catch (e: Exception) {
            onError()
            null
        }
    }

    suspend fun loadSearchedSongs(
        searchedText: String,
        onLoading: () -> Unit,
        onFinished: () -> Unit,
        onError: () -> Unit
    ): SearchedSong? {
        onLoading()
        val format = Json { ignoreUnknownKeys = true }

        return try {
            val response = apiClient.fetchSearchedSongFromServer(searchedText)

            if (response.code() == 200) {
                onFinished()
                return format.decodeFromString(response.body()!!.string())
            } else {
                onError()
                null
            }
        } catch (e: Exception) {
            onError()
            null
        }
    }
}