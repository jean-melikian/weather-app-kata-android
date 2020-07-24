package fr.ozoneprojects.open_weather_map_client.internal

import fr.ozoneprojects.open_weather_map_client.internal.core.NetworkModule
import fr.ozoneprojects.open_weather_map_client.internal.models.FullWeatherForecast
import retrofit2.http.GET
import retrofit2.http.Query


internal interface OpenWeatherMapApi {

    @GET("onecall")
    suspend fun getWeatherForecastForLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") language: String
    ): FullWeatherForecast

    companion object {

        val service: OpenWeatherMapApi by lazy {
            NetworkModule.retrofit.create(OpenWeatherMapApi::class.java)
        }

        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val API_KEY = "azertyuiop"
    }
}