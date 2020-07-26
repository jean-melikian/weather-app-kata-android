package fr.ozoneprojects.weatherappkata.ui.weatherdetails

import androidx.lifecycle.*
import fr.ozoneprojects.weatherlib.WeatherRepository
import fr.ozoneprojects.weatherlib.models.GenericError
import fr.ozoneprojects.weatherlib.models.Success
import fr.ozoneprojects.weatherlib.models.datasource.OpenWeatherOneCallResponse
import kotlinx.coroutines.launch

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val value: OpenWeatherOneCallResponse) : WeatherState()
    sealed class Error(val message: Throwable) : WeatherState() {
        class Unknown(message: Throwable) : Error(message)
    }
}

class WeatherDetailsModelFactory(private val weatherRepository: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(WeatherRepository::class.java)
            .newInstance(weatherRepository)
}

class WeatherDetailsViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val weatherState: MutableLiveData<WeatherState> = MutableLiveData()

    fun weatherState(): LiveData<WeatherState> = weatherState

    fun getCurrentWeatherForLocation(latitude: Double, longitude: Double) {
        weatherState.postValue(WeatherState.Loading)
        viewModelScope.launch {
            val response = weatherRepository.getWeatherForecastForLocation(
                latitude,
                longitude
            )
            weatherState.postValue(
                when (response) {
                    is GenericError -> WeatherState.Error.Unknown(response.throwable)
                    is Success -> WeatherState.Success(response.value)
                }
            )
        }
    }
}