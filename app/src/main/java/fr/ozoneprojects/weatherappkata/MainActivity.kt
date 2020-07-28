package fr.ozoneprojects.weatherappkata

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import fr.ozoneprojects.weatherappkata.dataadapter.LocationsRepositoryImpl
import fr.ozoneprojects.weatherappkata.ui.locations.LocationsFragment
import fr.ozoneprojects.weatherappkata.ui.locations.LocationsViewModel
import fr.ozoneprojects.weatherappkata.ui.locations.LocationsViewModelFactory
import fr.ozoneprojects.weatherappkata.ui.toolbar.ToolbarViewModel
import fr.ozoneprojects.weatherappkata.ui.toolbar.ToolbarViewModelFactory
import fr.ozoneprojects.weatherappkata.ui.weatherdetails.WeatherDetailsModelFactory
import fr.ozoneprojects.weatherappkata.ui.weatherdetails.WeatherDetailsViewModel
import fr.ozoneprojects.weatherlib.WeatherRepositoryFactory

class MainActivity : AppCompatActivity(R.layout.main_activity) {

    private lateinit var toolbarViewModel: ToolbarViewModel
    private lateinit var locationsViewModel: LocationsViewModel
    private lateinit var weatherDetailsViewModel: WeatherDetailsViewModel
    private lateinit var placesClient: PlacesClient

    private val locationsDataSource: SharedPreferences by lazy {
        getSharedPreferences(SHARED_PREFERENCES_LOCATIONS, Context.MODE_PRIVATE)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModels()
        placesClient = Places.createClient(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, WeatherDetailsFragment.newInstance())
                .replace(R.id.container, LocationsFragment.newInstance())
                .commitNow()
        }

        toolbarViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFragmentManager.popBackStack()
    }

    private fun initViewModels() {
        toolbarViewModel = ViewModelProvider(
            this,
            ToolbarViewModelFactory()
        ).get(ToolbarViewModel::class.java)

        locationsViewModel = ViewModelProvider(
            this,
            LocationsViewModelFactory(LocationsRepositoryImpl(locationsDataSource))
        ).get(LocationsViewModel::class.java)

        weatherDetailsViewModel = ViewModelProvider(
            this,
            WeatherDetailsModelFactory(WeatherRepositoryFactory.create(BuildConfig.OPEN_WEATHER_MAP_API_KEY))
        ).get(WeatherDetailsViewModel::class.java)
    }

    companion object {
        const val SHARED_PREFERENCES_LOCATIONS = "shared_preferences_locations"
    }
}