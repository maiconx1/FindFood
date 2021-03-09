package com.maicondcastro.findfood.places.placelist

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.maicondcastro.findfood.R
import com.maicondcastro.findfood.base.BaseFragment
import com.maicondcastro.findfood.base.NavigationCommand
import com.maicondcastro.findfood.databinding.FragmentPlaceListBinding
import com.maicondcastro.findfood.places.PlaceListAdapter
import com.maicondcastro.findfood.utils.Constants
import com.maicondcastro.findfood.utils.setDisplayHomeAsUpEnabled
import com.maicondcastro.findfood.utils.setTitle
import com.maicondcastro.findfood.utils.setup
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaceListFragment : BaseFragment() {

    override val viewModel: PlaceListViewModel by viewModel()

    private lateinit var binding: FragmentPlaceListBinding

    private lateinit var locationManager: LocationManager

    var locationListener: LocationListener = LocationListener { location ->
        val latitude = location.latitude
        val longitude = location.longitude

        updateLocation(latitude, longitude)
        viewModel.showLoading.value = false
        loadPlaces()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_place_list, container, false)
        binding.viewModel = viewModel

        setDisplayHomeAsUpEnabled(true)
        setTitle(getString(R.string.nearby_food))

        viewModel.showLoading.value = false

        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (viewModel.places.value.isNullOrEmpty()) {
            startLocationListener()
        }

        loadPlaces()

        return binding.root
    }

    private fun updateLocation(latitude: Double, longitude: Double) {
        viewModel.updateLatLng(latitude, longitude)
        locationManager.removeUpdates(locationListener)
    }

    private fun startLocationListener() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0f,
                    locationListener
                )
                viewModel.showLoading.value = true
            } catch (_: Exception) {
                viewModel.showLoading.value = false
            }
        } else {
            viewModel.navigationCommand.value = NavigationCommand.Back
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()

        binding.updateButton.setOnClickListener {
            viewModel.places.value = listOf()
            if (binding.distanceEditText.text.isNullOrEmpty()) {
                binding.distanceEditText.setText(Constants.DEFAULT_MAX_DISTANCE.toString())
            }
            viewModel.showLoading.value = true
            startLocationListener()
        }
    }

    private fun loadPlaces() {
        if (viewModel.places.value.isNullOrEmpty()) {
            viewModel.loadPlaces()
        }
    }

    private fun setupRecyclerView() {
        val adapter = PlaceListAdapter {
            viewModel.navigationCommand.postValue(
                NavigationCommand.To(
                    PlaceListFragmentDirections.actionPlaceListFragmentToPlaceDetailFragment(
                        it.placeId
                    )
                )
            )
        }

        binding.savedList.setup(adapter)
    }
}