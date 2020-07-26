package fr.ozoneprojects.weatherappkata.ui.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class CitiesViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor().newInstance()
}

class CitiesViewModel : ViewModel() {

}