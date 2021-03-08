package com.maicondcastro.findfood.places.savedplaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.maicondcastro.findfood.R
import com.maicondcastro.findfood.base.BaseFragment
import com.maicondcastro.findfood.base.NavigationCommand
import com.maicondcastro.findfood.places.PlaceListAdapter
import com.maicondcastro.findfood.databinding.FragmentSavedPlacesBinding
import com.maicondcastro.findfood.utils.setDisplayHomeAsUpEnabled
import com.maicondcastro.findfood.utils.setTitle
import com.maicondcastro.findfood.utils.setup
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedPlacesFragment : BaseFragment() {

    override val viewModel: SavedPlacesViewModel by viewModel()

    private lateinit var binding: FragmentSavedPlacesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_saved_places, container, false)
        binding.viewModel = viewModel

        setDisplayHomeAsUpEnabled(false)
        setTitle(getString(R.string.saved_places))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()
        binding.findPlacesButton.setOnClickListener {
            navigateToNearbyFood()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadSavedPlaces()
    }

    private fun navigateToNearbyFood() {
        viewModel.navigationCommand.postValue(
            NavigationCommand.To(
                SavedPlacesFragmentDirections.actionSavedPlacesFragmentToPlaceListFragment()
            )
        )
    }

    private fun setupRecyclerView() {
        val adapter = PlaceListAdapter {
            //viewModel.navigationCommand.postValue(NavigationCommand.To())
        }

        binding.savedList.setup(adapter)
    }
}