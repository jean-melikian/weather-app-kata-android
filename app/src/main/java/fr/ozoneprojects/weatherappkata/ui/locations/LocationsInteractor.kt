package fr.ozoneprojects.weatherappkata.ui.locations

import fr.ozoneprojects.weatherappkata.domain.models.Location

interface LocationsInteractor {
    fun showLocationDetails(location: Location)
    fun deleteLocation(location: Location)
}
