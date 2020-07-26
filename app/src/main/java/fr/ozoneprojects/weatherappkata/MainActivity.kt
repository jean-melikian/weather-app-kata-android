package fr.ozoneprojects.weatherappkata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import fr.ozoneprojects.weatherappkata.ui.cities.CitiesViewModel
import fr.ozoneprojects.weatherappkata.ui.cities.CitiesViewModelFactory
import fr.ozoneprojects.weatherappkata.ui.weatherdetails.WeatherDetailsFragment
import fr.ozoneprojects.weatherappkata.ui.weatherdetails.WeatherDetailsModelFactory
import fr.ozoneprojects.weatherappkata.ui.weatherdetails.WeatherDetailsViewModel
import fr.ozoneprojects.weatherlib.WeatherRepositoryFactory

class MainActivity : AppCompatActivity() {

    private lateinit var citiesViewModel: CitiesViewModel
    private lateinit var weatherDetailsViewModel: WeatherDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        initViewModels()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherDetailsFragment.newInstance())
                .commitNow()
        }
    }

    private fun initViewModels() {
        citiesViewModel = ViewModelProvider(
            this,
            CitiesViewModelFactory()
        ).get(CitiesViewModel::class.java)

        weatherDetailsViewModel = ViewModelProvider(
            this,
            WeatherDetailsModelFactory(WeatherRepositoryFactory.create(BuildConfig.OPEN_WEATHER_MAP_API_KEY))
        ).get(WeatherDetailsViewModel::class.java)

    }
}