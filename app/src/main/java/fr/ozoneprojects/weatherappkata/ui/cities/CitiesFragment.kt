package fr.ozoneprojects.weatherappkata.ui.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import fr.ozoneprojects.weatherappkata.R
import fr.ozoneprojects.weatherappkata.databinding.CitiesFragmentBinding
import fr.ozoneprojects.weatherappkata.ui.ViewBindingFragment

class CitiesFragment : ViewBindingFragment<CitiesFragmentBinding>(R.layout.cities_fragment) {

    private val viewModel: CitiesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = bindView(CitiesFragmentBinding.inflate(inflater, container, false))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        fun newInstance() = CitiesFragment()
    }
}