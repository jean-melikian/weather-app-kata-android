package fr.ozoneprojects.open_weather_map_client.internal

import fr.ozoneprojects.open_weather_map_client.WeatherRepository
import fr.ozoneprojects.open_weather_map_client.models.GenericError
import fr.ozoneprojects.open_weather_map_client.models.Response
import fr.ozoneprojects.open_weather_map_client.models.Success
import fr.ozoneprojects.open_weather_map_client.models.datasource.OpenWeatherOneCallResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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