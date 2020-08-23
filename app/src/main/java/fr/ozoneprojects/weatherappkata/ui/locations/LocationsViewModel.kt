package fr.ozoneprojects.weatherappkata.ui.locations

import androidx.lifecycle.*
import com.google.android.libraries.places.api.model.Place
import fr.ozoneprojects.weatherappkata.domain.models.Location
import fr.ozoneprojects.weatherappkata.ui.locations.dataadapter.LocationsRepository
import fr.ozoneprojects.weatherappkata.ui.locations.places.dataadapter.PlacesRepository
import kotlinx.coroutines.launch

sealed class LocationsState {
    object Loading : LocationsState()
    class Success(val value: List<Location>) : LocationsState()
    class Error(val message: Throwable) : LocationsState()
}

sealed class CurrentLocationState {
    object Loading : CurrentLocationState()
    class Success(val value: Location) : CurrentLocationState()
    open class Error(val message: Throwable) : CurrentLocationState()
    class MissingPermission(message: Throwable) : Error(message)
    class RequestPermission(message: Throwable) : Error(message)
}

class LocationsViewModelFactory(
    private val locationsRepository: LocationsRepository,
    private val placesRepository: PlacesRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(LocationsRepository::class.java, PlacesRepository::class.java)
            .newInstance(locationsRepository, placesRepository)
}

class LocationsViewModel(
    private val locationsRepository: LocationsRepository,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val locationsState: MutableLiveData<LocationsState> = MutableLiveData()
    private val currentLocationState: MutableLiveData<CurrentLocationState> = MutableLiveData()

    fun locationsState(): LiveData<LocationsState> = locationsState
    fun currentLocationState(): LiveData<CurrentLocationState> = currentLocationState

    fun addNewLocation(newLocation: Location) {
        viewModelScope.launch {
            locationsState.postValue(LocationsState.Loading)
            locationsRepository.addNewUserLocation(newLocation)
            locationsState.postValue(LocationsState.Success(locationsRepository.getAllUserLocations()))
        }
    }

    fun getAllUserLocations() {
        viewModelScope.launch {
            locationsState.postValue(LocationsState.Loading)
            locationsState.postValue(LocationsState.Success(locationsRepository.getAllUserLocations()))
        }
    }

    fun deleteUserLocation(locationId: String) {
        viewModelScope.launch {
            locationsState.postValue(LocationsState.Loading)
            locationsRepository.removeUserLocation(locationId)
            locationsState.postValue(LocationsState.Success(locationsRepository.getAllUserLocations()))
        }
    }

    fun getPlacesRequiredFields(): List<Place.Field> =
        placesRepository.getRequiredFields()

    fun getCurrentLocation() {
        currentLocationState.postValue(CurrentLocationState.Loading)
        placesRepository.getCurrentLocation(onComplete = {
            currentLocationState.postValue(
                it?.let { location ->
                    CurrentLocationState.Success(location)
                }
                    ?: CurrentLocationState.Error(Exception("An error occurred while trying to get your current location"))
            )
        }, onMissingPermission = {
            currentLocationState.postValue(
                CurrentLocationState.MissingPermission(
                    Exception("We need your permission to use your location to get weather conditions for your current place")
                )
            )
        })
    }
    /*
    fun removeUserLocation(locationId: String)
    fun clearUserLocations()
     */
}