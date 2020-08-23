package fr.ozoneprojects.weatherappkata.ui.locations.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import fr.ozoneprojects.weatherappkata.R
import fr.ozoneprojects.weatherappkata.ui.locations.CurrentLocationState
import fr.ozoneprojects.weatherappkata.ui.locations.LocationsViewModel

class MapsFragment : Fragment() {

    private val locationsViewModel: LocationsViewModel by activityViewModels()

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        locationsViewModel.currentLocationState().observe(this, Observer { currentLocationState ->
            when (currentLocationState) {
                is CurrentLocationState.Success -> {
                    val coordinates =
                        LatLng(currentLocationState.value.latitude, currentLocationState.value.longitude)
                    googleMap.addMarker(MarkerOptions().position(coordinates).title("Currently in ${currentLocationState.value.name}"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 13.0f))
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    companion object {
        fun newInstance() = MapsFragment()
    }
}