package fr.ozoneprojects.weatherlib.internal.core

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import fr.ozoneprojects.weatherlib.internal.OpenWeatherMapApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal object NetworkModule {

    private fun createHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .apply {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }.build()

    private fun createMoshiConverterFactory(): MoshiConverterFactory =
        MoshiConverterFactory.create(
            Moshi.Builder().apply {
                add(KotlinJsonAdapterFactory())
            }.build()
        ).withNullSerialization()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .apply {

                baseUrl(OpenWeatherMapApi.BASE_URL)
                addConverterFactory(
                    createMoshiConverterFactory()
                )
                client(createHttpClient())
            }.build()
    }
}