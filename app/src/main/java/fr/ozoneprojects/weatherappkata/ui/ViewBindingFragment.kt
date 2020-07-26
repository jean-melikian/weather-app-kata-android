package fr.ozoneprojects.weatherappkata.ui

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

open class ViewBindingFragment<T : ViewBinding>(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {
    private var _binding: T? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = _binding!!

    fun bindView(value: T): View {
        _binding = value
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}