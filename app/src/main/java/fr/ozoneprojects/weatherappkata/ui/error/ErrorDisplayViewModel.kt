package fr.ozoneprojects.weatherappkata.ui.error

import android.Manifest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.ozoneprojects.weatherappkata.ui.error.exceptions.MissingPermissionsException

abstract class ErrorDisplay(
    val message: Throwable = Exception(),
    val action: (() -> Unit)? = null
) {
    class None() : ErrorDisplay()
    class NoInternet(message: Throwable) : ErrorDisplay(message)
    class MissingLocationPermission(
        action: (() -> Unit)? = null
    ) : ErrorDisplay(MissingPermissionsException(Manifest.permission.ACCESS_FINE_LOCATION), action)
}

class ErrorDisplayViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.newInstance()
}

class ErrorDisplayViewModel : ViewModel() {
    private val errorState = MutableLiveData<ErrorDisplay>()

    fun errorState(): LiveData<ErrorDisplay> = errorState

    fun setErrorDisplayState(errorDisplay: ErrorDisplay) {
        errorState.postValue(errorDisplay)
    }
}