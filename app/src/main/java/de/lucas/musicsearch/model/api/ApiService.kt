package de.lucas.musicsearch.model.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface ApiService {

    @GET
    suspend fun fetchTop20SongsFromServer(
        @Url url: String,
        @Header("X-RapidAPI-Key") key: String
    ): Response<ResponseBody>

    @GET
    suspend fun fetchSongDetailsFromServer(
        @Url url: String,
        @Header("X-RapidAPI-Key") key: String
    ): Response<ResponseBody>
}