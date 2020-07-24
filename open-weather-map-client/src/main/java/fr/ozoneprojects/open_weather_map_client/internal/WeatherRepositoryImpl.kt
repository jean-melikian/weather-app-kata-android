package fr.ozoneprojects.open_weather_map_client.internal

import fr.ozoneprojects.open_weather_map_client.WeatherRepository
import fr.ozoneprojects.open_weather_map_client.internal.models.FullWeatherForecast
import fr.ozoneprojects.open_weather_map_client.internal.models.GenericError
import fr.ozoneprojects.open_weather_map_client.internal.models.Response
import fr.ozoneprojects.open_weather_map_client.internal.models.Success

internal class WeatherRepositoryImpl(
    private val openWeatherMapApi: OpenWeatherMapApi
) : WeatherRepository {
    override suspend fun getWeatherForecastForLocation(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        units: String,
        language: String
    ): Response<FullWeatherForecast> = try {
        Success(
            openWeatherMapApi.getWeatherForecastForLocation(
                latitude,
                longitude,
                apiKey,
                units,
                language
            )
        )
    } catch (t: Throwable) {
        GenericError(t)
    }
}