package fr.ozoneprojects.weatherappkata.ui.locations.places

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import fr.ozoneprojects.weatherappkata.domain.models.Location
import fr.ozoneprojects.weatherappkata.ui.error.ErrorDisplay
import fr.ozoneprojects.weatherappkata.ui.error.ErrorDisplayViewModel
import fr.ozoneprojects.weatherappkata.ui.locations.LocationsViewModel

sealed class PlacesErrorDisplay(message: Throwable = Exception()) : ErrorDisplay(message) {
    class GenericError(message: Throwable) : PlacesErrorDisplay(message)
}

class CitiesAutoCompleteFragment : AutocompleteSupportFragment() {

    private val locationsViewModel: LocationsViewModel by activityViewModels()
    private val errorDisplayViewModel: ErrorDisplayViewModel by activityViewModels()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        this.setTypeFilter(TypeFilter.CITIES)
        this.setPlaceFields(
            locationsViewModel.getPlacesRequiredFields()
        )
    }

    init {
        this.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                place.latLng?.let { latLng ->
                    val newLocation =
                        Location(
                            place.id!!,
                            place.name!!,
                            latLng.latitude,
                            latLng.longitude
                        )
                    locationsViewModel.addNewLocation(newLocation)
                }
            }

            override fun onError(status: Status) {
                if (!status.isCanceled) {
                    errorDisplayViewModel.setErrorDisplayState(
                        PlacesErrorDisplay.GenericError(
                            Exception("Something went wrong with Google Places")
                        )
                    )
                }
            }
        })
    }


}