package fr.ozoneprojects.open_weather_map_client.models.datasource

import com.squareup.moshi.Json

data class OpenWeatherOneCallResponse(
    @Json(name = "lat") var latitude: Float,
    @Json(name = "lon") var longitude: Float,
    @Json(name = "timezone") var timezone: String,
    @Json(name = "timezone_offset") var timezoneOffset: Long,
    @Json(name = "current") var current: OpenWeatherDetails
)