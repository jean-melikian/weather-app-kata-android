package fr.ozoneprojects.weatherappkata

import android.app.Application
import com.google.android.libraries.places.api.Places

class WeatherAppKataApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Places.initialize(this, BuildConfig.PLACES_API_KEY)
    }
}