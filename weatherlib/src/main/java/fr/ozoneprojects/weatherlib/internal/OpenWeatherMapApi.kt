package fr.ozoneprojects.weatherlib.internal

import fr.ozoneprojects.weatherlib.internal.core.NetworkModule
import fr.ozoneprojects.weatherlib.models.datasource.OpenWeatherOneCallResponse
import retrofit2.http.GET
import retrofit2.http.Query


internal interface OpenWeatherMapApi {

    @GET("onecall")
    suspend fun getWeatherForecastForLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "fr"
    ): OpenWeatherOneCallResponse

    companion object {

        val service: OpenWeatherMapApi by lazy {
            NetworkModule.retrofit.create(OpenWeatherMapApi::class.java)
        }

        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }
}