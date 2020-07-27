package fr.ozoneprojects.weatherappkata.ui.locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.ozoneprojects.weatherappkata.databinding.LocationItemBinding
import fr.ozoneprojects.weatherappkata.domain.models.Location

class LocationsAdapter(private val locationsInteractor: LocationsInteractor) :
    ListAdapter<Location, LocationsAdapter.LocationViewHolder>(LocationDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder =
        LocationViewHolder(
            LocationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            locationsInteractor
        )

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    class LocationDiff : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean =
            oldItem == newItem
    }

    class LocationViewHolder(
        private val binding: LocationItemBinding,
        private val locationsInteractor: LocationsInteractor
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(location: Location) = with(binding) {
            root.setOnClickListener {
                locationsInteractor.showLocationDetails(location)
            }
            deleteLocationImageButton.setOnClickListener {
                locationsInteractor.deleteLocation(location)
            }
            locationNameTextView.text = location.name
        }
    }

}