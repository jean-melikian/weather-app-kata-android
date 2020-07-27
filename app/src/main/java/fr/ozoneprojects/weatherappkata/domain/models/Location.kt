package fr.ozoneprojects.weatherappkata.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(val id: String, val name: String, val latitude: Double, val longitude: Double)
