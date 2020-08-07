package fr.ozoneprojects.weatherappkata.ui.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import fr.ozoneprojects.weatherappkata.R
import fr.ozoneprojects.weatherappkata.core.VerticalSpaceItemDecorator
import fr.ozoneprojects.weatherappkata.databinding.LocationsFragmentBinding
import fr.ozoneprojects.weatherappkata.domain.models.Location
import fr.ozoneprojects.weatherappkata.ui.ViewBindingFragment
import fr.ozoneprojects.weatherappkata.ui.toolbar.ToolbarViewModel
import fr.ozoneprojects.weatherappkata.ui.weatherdetails.WeatherDetailsFragment
import fr.ozoneprojects.weatherappkata.ui.weatherdetails.WeatherDetailsViewModel

class LocationsFragment :
    ViewBindingFragment<LocationsFragmentBinding>(R.layout.locations_fragment),
    LocationsInteractor {

    private val toolbarViewModel: ToolbarViewModel by activityViewModels()
    private val locationsViewModel: LocationsViewModel by activityViewModels()
    private val weatherViewModel: WeatherDetailsViewModel by activityViewModels()

    private val locationsAdapter: LocationsAdapter = LocationsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = bindView(LocationsFragmentBinding.inflate(inflater, container, false))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupLocationsRecycler()

        locationsViewModel.locationsState().observe(viewLifecycleOwner, locationsObserver)

        binding.locationsRefreshLayout.setOnRefreshListener {
            locationsViewModel.getAllUserLocations()
        }

        locationsViewModel.getAllUserLocations()
    }

    override fun onResume() {
        super.onResume()
        toolbarViewModel.setTitle(getString(R.string.app_name))
    }

    private fun setupLocationsRecycler() {
        binding.locationsRecyclerView.apply {
            adapter = locationsAdapter
            (0 until itemDecorationCount).map {
                removeItemDecorationAt(it)
            }
            addItemDecoration(VerticalSpaceItemDecorator(resources.getDimensionPixelOffset(R.dimen.spacing_small)))
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
