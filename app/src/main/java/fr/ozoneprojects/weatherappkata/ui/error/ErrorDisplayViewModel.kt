package fr.ozoneprojects.weatherappkata.ui.error

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class ErrorDisplay(val message: Throwable = Exception()) {
    class None() : ErrorDisplay()
    class NoInternet(message: Throwable) : ErrorDisplay()
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