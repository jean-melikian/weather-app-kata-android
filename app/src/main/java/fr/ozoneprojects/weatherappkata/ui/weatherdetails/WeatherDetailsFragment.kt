package fr.ozoneprojects.weatherappkata.ui.weatherdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import fr.ozoneprojects.weatherappkata.R
import fr.ozoneprojects.weatherappkata.core.unixToPrettyFormat
import fr.ozoneprojects.weatherappkata.databinding.WeatherDetailsFragmentBinding
import fr.ozoneprojects.weatherappkata.ui.ViewBindingFragment
import fr.ozoneprojects.weatherlib.WeatherIconMapper
import fr.ozoneprojects.weatherlib.models.datasource.OpenWeatherOneCallResponse
import java.util.*
import kotlin.math.roundToInt

class WeatherDetailsFragment :
    ViewBindingFragment<WeatherDetailsFragmentBinding>(R.layout.weather_details_fragment) {

    private val parisCoordinates: Pair<Double, Double> = 48.8534 to 2.3488

    private val viewModel: WeatherDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = bindView(WeatherDetailsFragmentBinding.inflate(inflater, container, false))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.weatherState().observe(viewLifecycleOwner, weatherStateObserver)
        viewModel.getCurrentWeatherForLocation(parisCoordinates.first, parisCoordinates.second)
        binding.weatherRefreshLayout.setOnRefreshListener {
            viewModel.getCurrentWeatherForLocation(parisCoordinates.first, parisCoordinates.second)
        }
    }

    private val weatherStateObserver: Observer<in WeatherState> = Observer { weatherState ->

        when (weatherState) {
            WeatherState.Loading -> {
                binding.weatherRefreshLayout.isRefreshing = true
            }
            is WeatherState.Success -> {
                binding.weatherRefreshLayout.isRefreshing = false
                showWeather(weatherState.value)
            }
            is WeatherState.Error.Unknown -> {
                binding.weatherRefreshLayout.isRefreshing = false
                Snackbar.make(
                    binding.root,
                    weatherState.message.localizedMessage ?: getString(R.string.unknown_error),
                    Snackbar.LENGTH_LONG
                )
            }
        }

    }

    private fun showWeather(openWeatherOneCallResponse: OpenWeatherOneCallResponse) {
        binding.apply {
            val current = openWeatherOneCallResponse.current

            temperatureTextView.text =
                getString(R.string.temperature_celsius_format, current.temp.roundToInt())
            temperatureFeelsLikeTextView.text = getString(
                R.string.temperature_feels_like_celsius_format,
                current.feelsLike.roundToInt()
            )

            dateTimeTextView.text = (Date(current.datetime)).unixToPrettyFormat(
                getString(R.string.date_time_pretty_format),
                openWeatherOneCallResponse.timezone
            )


            openWeatherOneCallResponse.current.weather.getOrNull(0)?.let { weather ->
                weatherDescriptionTextView.text = weather.description

                WeatherIconMapper.getIconRes(weather.icon)?.let { iconRes ->
                    weatherImageView.setImageResource(iconRes)
                }
            }
        }
    }


    companion object {
        fun newInstance() = WeatherDetailsFragment()
    }
}
