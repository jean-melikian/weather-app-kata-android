package fr.ozoneprojects.open_weather_map_client

import fr.ozoneprojects.open_weather_map_client.internal.OpenWeatherMapApi
import fr.ozoneprojects.open_weather_map_client.internal.WeatherRepositoryImpl
import fr.ozoneprojects.open_weather_map_client.models.Response
import fr.ozoneprojects.open_weather_map_client.models.datasource.OpenWeatherOneCallResponse

interface WeatherRepository {
    suspend fun getWeatherForecastForLocation(
        latitude: Double,
        longitude: Double,
        units: String = "metric",
        language: String = "fr"
    ): Response<OpenWeatherOneCallResponse>
}

object WeatherRepositoryFactory {
    fun create(apiKey: String): WeatherRepository =
        WeatherRepositoryImpl(apiKey, OpenWeatherMapApi.service)
}

