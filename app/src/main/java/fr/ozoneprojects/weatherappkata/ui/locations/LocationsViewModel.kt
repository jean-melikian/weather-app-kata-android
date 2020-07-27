package fr.ozoneprojects.weatherappkata.ui.locations

import androidx.lifecycle.*
import fr.ozoneprojects.weatherappkata.dataadapter.LocationsRepository
import fr.ozoneprojects.weatherappkata.domain.models.Location
import kotlinx.coroutines.launch

sealed class LocationsState {
    object Loading : LocationsState()
    class Success(val value: List<Location>) : LocationsState()
    class Error(val message: Throwable) : LocationsState()
}

class LocationsViewModelFactory(private val locationsRepository: LocationsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(
            LocationsRepository::class.java
        ).newInstance(locationsRepository)
}

class LocationsViewModel(
    private val locationsRepository: LocationsRepository
) : ViewModel() {

    private val locationsState: MutableLiveData<LocationsState> = MutableLiveData()

    fun locationsState(): LiveData<LocationsState> = locationsState

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
    /*
    fun removeUserLocation(locationId: String)
    fun clearUserLocations()
     */
}