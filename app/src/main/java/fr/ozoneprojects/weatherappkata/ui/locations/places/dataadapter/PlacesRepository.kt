package fr.ozoneprojects.weatherappkata.ui.locations.places.dataadapter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import fr.ozoneprojects.weatherappkata.R
import fr.ozoneprojects.weatherappkata.domain.models.Location
import java.util.*

interface PlacesRepository {
    fun getRequiredFields(): List<Place.Field>
    fun getCurrentLocation(onComplete: (location: Location?) -> Unit, onMissingPermission: () -> Unit)
}

class PlacesRepositoryImpl(private val context: Context) : PlacesRepository {

    private val placesClient: PlacesClient = Places.createClient(context)

    override fun getRequiredFields(): List<Place.Field> =
        listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG
        )

    override fun getCurrentLocation(
        onComplete: (location: Location?) -> Unit,
        onMissingPermission: () -> Unit
    ) {
        val currentPlaceRequest = FindCurrentPlaceRequest.builder(getRequiredFields()).build()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED

        ) {
            onMissingPermission()
        } else {
            val placeResponse = placesClient.findCurrentPlace(currentPlaceRequest)
            placeResponse.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = task.result
                    for (placeLikelihood: PlaceLikelihood in response?.placeLikelihoods
                        ?: emptyList()) {
                        Log.i(TAG, "Place '${placeLikelihood.place.name}' has likelihood: ${placeLikelihood.likelihood}")
                    }

                    onComplete(
                        response?.placeLikelihoods?.maxBy { it.likelihood }?.place?.let {
                            it.id?.let { id ->
                                it.latLng?.let { latLng ->
                                    val geocoder = Geocoder(context, Locale.getDefault())
                                    val localityName =
                                        geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.firstOrNull()?.locality
                                            ?: context.getString(R.string.unavailable)

                                    Location(id, localityName, latLng.latitude, latLng.longitude)
                                }
                            }
                        }
                    )
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        Log.e(TAG, "Place not found: ${exception.statusCode}")
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "PlacesRepositoryImpl"
    }
}
