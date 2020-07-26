package fr.ozoneprojects.open_weather_map_client.internal

import fr.ozoneprojects.open_weather_map_client.WeatherRepository
import fr.ozoneprojects.open_weather_map_client.models.GenericError
import fr.ozoneprojects.open_weather_map_client.models.Response
import fr.ozoneprojects.open_weather_map_client.models.Success
import fr.ozoneprojects.open_weather_map_client.models.datasource.OpenWeatherOneCallResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class WeatherRepositoryImpl(
    private val apiKey: String,
    private val openWeatherMapApi: OpenWeatherMapApi
) : WeatherRepository {
    override suspend fun getWeatherForecastForLocation(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String
    ): Response<OpenWeatherOneCallResponse> = withContext(Dispatchers.IO) {
        try {
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
            t.printStackTrace()
            GenericError<OpenWeatherOneCallResponse>(t)
        }
    }
}