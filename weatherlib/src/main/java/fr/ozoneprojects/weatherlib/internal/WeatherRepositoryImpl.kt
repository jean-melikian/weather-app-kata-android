package fr.ozoneprojects.weatherlib.internal

import fr.ozoneprojects.weatherlib.WeatherRepository
import fr.ozoneprojects.weatherlib.models.GenericError
import fr.ozoneprojects.weatherlib.models.Response
import fr.ozoneprojects.weatherlib.models.Success
import fr.ozoneprojects.weatherlib.models.datasource.OpenWeatherOneCallResponse
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
            val result = openWeatherMapApi.getWeatherForecastForLocation(
                latitude,
                longitude,
                apiKey,
                units,
                language
            )
            if (result != null) {
                Success(result)
            } else GenericError<OpenWeatherOneCallResponse>(Exception("An error occurred"))
        } catch (t: Throwable) {
            t.printStackTrace()
            GenericError<OpenWeatherOneCallResponse>(t)
        }
    }
}