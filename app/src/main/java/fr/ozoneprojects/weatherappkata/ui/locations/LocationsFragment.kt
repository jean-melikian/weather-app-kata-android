package fr.ozoneprojects.weatherappkata.ui.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.snackbar.Snackbar
import fr.ozoneprojects.weatherappkata.R
import fr.ozoneprojects.weatherappkata.core.SpacingItemDecorator
import fr.ozoneprojects.weatherappkata.databinding.LocationsFragmentBinding
import fr.ozoneprojects.weatherappkata.domain.models.Location
import fr.ozoneprojects.weatherappkata.ui.ViewBindingFragment
import fr.ozoneprojects.weatherappkata.ui.weatherdetails.WeatherDetailsFragment
import fr.ozoneprojects.weatherappkata.ui.weatherdetails.WeatherDetailsViewModel

class LocationsFragment :
    ViewBindingFragment<LocationsFragmentBinding>(R.layout.locations_fragment),
    LocationsInteractor {

    private val locationsViewModel: LocationsViewModel by activityViewModels()
    private val weatherViewModel: WeatherDetailsViewModel by activityViewModels()

    private val locationsAdapter: LocationsAdapter = LocationsAdapter(this)

    private var autoCompleteFragment: AutocompleteSupportFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = bindView(LocationsFragmentBinding.inflate(inflater, container, false))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupLocationsRecycler()

        autoCompleteFragment =
            (childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment).also { autoCompleteFragment ->

                autoCompleteFragment.setTypeFilter(TypeFilter.CITIES)
                autoCompleteFragment.setPlaceFields(
                    listOf(
                        Place.Field.ID,
                        Place.Field.NAME,
                        Place.Field.LAT_LNG
                    )
                )

                autoCompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
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
                        Snackbar.make(
                            binding.root,
                            status.statusMessage ?: getString(R.string.unknown_error),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                })
            }

        locationsViewModel.locationsState().observe(viewLifecycleOwner, locationsObserver)

        binding.locationsRefreshLayout.setOnRefreshListener {
            locationsViewModel.getAllUserLocations()
        }

        locationsViewModel.getAllUserLocations()
    }

    private fun setupLocationsRecycler() {
        binding.locationsRecyclerView.apply {
            adapter = locationsAdapter
            (0 until itemDecorationCount).map {
                removeItemDecorationAt(it)
            }
            addItemDecoration(SpacingItemDecorator(resources.getDimensionPixelOffset(R.dimen.spacing_small)))
        }
    }

    private val locationsObserver: Observer<LocationsState> = Observer {
        when (it) {
            LocationsState.Loading -> binding.locationsRefreshLayout.isRefreshing = true
            is LocationsState.Success -> {
                binding.locationsRefreshLayout.isRefreshing = false
                showLocations(it.value)
            }
            is LocationsState.Error -> {
                binding.locationsRefreshLayout.isRefreshing = false
                Snackbar.make(
                    binding.root,
                    it.message.localizedMessage ?: getString(R.string.unknown_error),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showLocations(locations: List<Location>) {
        locationsAdapter.submitList(locations)
    }

    override fun showLocationDetails(location: Location) {
        weatherViewModel.setSelectedLocation(location)
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, WeatherDetailsFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun deleteLocation(location: Location) {
        locationsViewModel.deleteUserLocation(location.id)
    }

    companion object {
        fun newInstance() = LocationsFragment()
    }
}
