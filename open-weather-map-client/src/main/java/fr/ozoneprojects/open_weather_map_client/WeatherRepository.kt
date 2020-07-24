package fr.ozoneprojects.open_weather_map_client

import fr.ozoneprojects.open_weather_map_client.internal.models.FullWeatherForecast
import fr.ozoneprojects.open_weather_map_client.internal.models.Response

interface WeatherRepository {
    suspend fun getWeatherForecastForLocation(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        units: String = "metric",
        language: String = "fr"
    ): Response<FullWeatherForecast>
}


