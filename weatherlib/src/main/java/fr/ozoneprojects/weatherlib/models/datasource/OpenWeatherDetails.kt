package fr.ozoneprojects.weatherlib.models.datasource

import com.squareup.moshi.Json

data class OpenWeatherDetails(
    @Json(name = "dt") var datetime: Long,
    @Json(name = "sunrise") var sunrise: Long,
    @Json(name = "sunset") var sunset: Long,
    @Json(name = "temp") var temp: Float,
    @Json(name = "feels_like") var feelsLike: Float,
    @Json(name = "pressure") var pressure: Int,
    @Json(name = "humidity") var humidity: Int,
    @Json(name = "dew_point") var dewPoint: Float,
    @Json(name = "uvi") var uvi: Float,
    @Json(name = "clouds") var clouds: Int,
    @Json(name = "visibility") var visibility: Int,
    @Json(name = "wind_speed") var windSpeed: Float,
    @Json(name = "wind_deg") var windDegree: Int,
    @Json(name = "weather") var weather: List<OpenWeatherDescription>
)