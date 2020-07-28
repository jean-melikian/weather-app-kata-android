package fr.ozoneprojects.weatherappkata.ui.toolbar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ToolbarViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor().newInstance()
}


class ToolbarViewModel : ViewModel() {

    private val title: MutableLiveData<String> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun setTitle(title: String) {
        this.title.postValue(title)
    }
}
