package fr.ozoneprojects.weatherappkata.dataadapter

import fr.ozoneprojects.weatherappkata.domain.models.Location

interface LocationsRepository {
    suspend fun addNewUserLocation(newLocation: Location)
    suspend fun getUserLocationById(locationId: String): Location?
    suspend fun getAllUserLocations(): List<Location>
    suspend fun removeUserLocation(locationId: String)
    suspend fun clearUserLocations()
}