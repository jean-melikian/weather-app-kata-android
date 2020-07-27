package fr.ozoneprojects.weatherlib

import fr.ozoneprojects.weatherlib.internal.OpenWeatherMapApi
import fr.ozoneprojects.weatherlib.internal.WeatherRepositoryImpl
import fr.ozoneprojects.weatherlib.models.Response
import fr.ozoneprojects.weatherlib.models.datasource.OpenWeatherOneCallResponse

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

