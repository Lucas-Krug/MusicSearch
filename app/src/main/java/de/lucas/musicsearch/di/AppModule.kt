package de.lucas.musicsearch.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.lucas.musicsearch.model.api.ApiConstants
import de.lucas.musicsearch.model.api.ApiService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkhttpClient() = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor { Timber.tag("http").d(it) }
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .build()

    @Singleton
    @Provides
    fun provideApiConstants() = ApiConstants(
        baseUrl = "https://shazam.p.rapidapi.com/",
        accessKey = "5923336e3bmsh806b5cbcbbfce3fp1054c1jsnfe2bd10ddb9a"
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideRetrofit(apiConstants: ApiConstants, okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(apiConstants.baseUrl)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideAppointmentService(retrofit: Retrofit): ApiService = retrofit.create()
}