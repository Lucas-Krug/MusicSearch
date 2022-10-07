package de.lucas.musicsearch.model.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    @GET
    suspend fun fetchSongsFromServer(
        @Url url: String
    ): ResponseBody
}