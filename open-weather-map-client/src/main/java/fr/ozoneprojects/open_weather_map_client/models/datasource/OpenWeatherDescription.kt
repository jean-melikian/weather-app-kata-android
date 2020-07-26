package fr.ozoneprojects.open_weather_map_client.models.datasource

import com.squareup.moshi.Json

data class OpenWeatherDescription(
    @Json(name = "id") var id: Int,
    @Json(name = "main") var main: String,
    @Json(name = "description") var description: String,
    @Json(name = "icon") var icon: String
)