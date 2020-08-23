package fr.ozoneprojects.weatherappkata.ui.error.exceptions

data class MissingPermissionsException(private val accessFineLocation: String) : Exception()
