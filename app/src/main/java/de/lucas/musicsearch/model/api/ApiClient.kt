package de.lucas.musicsearch.model.api

import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class ApiClient @Inject constructor(
    private val apiConstants: ApiConstants,
    private val apiService: ApiService
) {

    suspend fun fetchTop20SongsFromServer(): Response<ResponseBody> =
        apiService.fetchTop20SongsFromServer("charts/track", apiConstants.accessKey)
}