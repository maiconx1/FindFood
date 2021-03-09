package com.maicondcastro.findfood.places.placedetails

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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.maicondcastro.findfood.R
import com.maicondcastro.findfood.base.BaseFragment
import com.maicondcastro.findfood.base.NavigationCommand
import com.maicondcastro.findfood.databinding.FragmentPlaceDetailBinding
import com.maicondcastro.findfood.utils.setDisplayHomeAsUpEnabled
import com.maicondcastro.findfood.utils.setTitle
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaceDetailFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override val viewModel: PlaceDetailsViewModel by viewModel()

    private lateinit var binding: FragmentPlaceDetailBinding

    private lateinit var locationManager: LocationManager

    private val args: PlaceDetailFragmentArgs by navArgs()

    private var locationListener: LocationListener = LocationListener { location ->
        val latitude = location.latitude
        val longitude = location.longitude

        updateLocation(latitude, longitude)
        viewModel.showLoading.value = false
        loadDetails()
    }

    private var mapFragment: SupportMapFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_place_detail, container, false)
        viewModel.placeId = args.placeId
        binding.viewModel = viewModel

        setDisplayHomeAsUpEnabled(true)
        setTitle("")

        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mapFragment =
            childFragmentManager.findFragmentById(R.id.location_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        mapFragment?.view?.visibility = View.GONE

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
                map.isMyLocationEnabled = true
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

        binding.followPlaceButton.setOnClickListener {
            viewModel.savePlace()
        }

        binding.map.setOnClickListener {
            viewModel.placeItem.value?.url?.let {
                viewModel.openUrl.value = it
            }
        }

        binding.site.setOnClickListener {
            viewModel.placeItem.value?.webSite?.let {
                viewModel.openUrl.value = it
            }
        }

        viewModel.placeItem.observe(viewLifecycleOwner, {
            val lat = it.lat ?: 0.0
            val lng = it.lng ?: 0.0

            if (lat != 0.0 && lng != 0.0) {
                map.addMarker(MarkerOptions().position(LatLng(lat, lng)).title(it.name))
                val zoomLevel = 15f
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), zoomLevel))
            }
            mapFragment?.view?.visibility = View.VISIBLE

            Glide.with(requireContext())
                .load(it.icon)
                .into(binding.icon)
        })

        viewModel.followButtonSaved.observe(viewLifecycleOwner, {
            binding.followPlaceButton.text = if (it) {
                getString(R.string.unfollow_place)
            } else {
                getString(R.string.follow_place)
            }
        })
    }

    private fun loadDetails() {
        viewModel.loadDetails()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        startLocationListener()
    }
}