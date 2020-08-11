package fr.ozoneprojects.weatherappkata.ui.locations.dataadapter

import android.content.SharedPreferences
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import fr.ozoneprojects.weatherappkata.domain.models.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationsRepositoryImpl(private val dataSource: SharedPreferences) :
    LocationsRepository {

    private val moshi: Moshi = Moshi.Builder().apply {
        add(KotlinJsonAdapterFactory())
    }.build()

    private val locationAdapter: JsonAdapter<Location> = moshi.adapter(Location::class.java)

    override suspend fun addNewUserLocation(newLocation: Location) {
        withContext(Dispatchers.IO) {
            dataSource.edit().putString(newLocation.id, locationAdapter.toJson(newLocation))
                .apply()
        }
    }

    override suspend fun getUserLocationById(locationId: String): Location? =
        withContext(Dispatchers.IO) {
            dataSource.getString(locationId, null)?.let {
                locationAdapter.fromJson(it)
            }
        }

    override suspend fun getAllUserLocations(): List<Location> =
        withContext(Dispatchers.IO) {
            dataSource.all.values.filterIsInstance<String>().mapNotNull {
                locationAdapter.fromJson(it)
            }
        }

    override suspend fun removeUserLocation(locationId: String) {
        withContext(Dispatchers.IO) {
            dataSource.edit().remove(locationId).apply()
        }
    }

    override suspend fun clearUserLocations() {
        withContext(Dispatchers.IO) {
            dataSource.edit().clear().apply()
        }
    }
}