package com.maicondcastro.findfood.places.placelist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maicondcastro.findfood.base.BaseViewModel
import com.maicondcastro.findfood.network.PlacesRemoteDataSource
import com.maicondcastro.findfood.places.models.PlaceItem
import com.maicondcastro.findfood.utils.Constants.Companion.DEFAULT_MAX_DISTANCE
import com.maicondcastro.findfood.utils.asItem
import kotlinx.coroutines.launch

class PlaceListViewModel(
    app: Application,
    private val dataSource: PlacesRemoteDataSource
) : BaseViewModel(app) {

    var places = MutableLiveData<List<PlaceItem>>()
    val maxDistance = MutableLiveData(DEFAULT_MAX_DISTANCE.toString())

    var lat: Double? = null
    var lng: Double? = null

    fun loadPlaces() {
        try {
            QueryItems(lat, lng, maxDistance.value?.toInt()).checkValid()?.let {
                showLoading.value = true
                viewModelScope.launch {
                    try {
                        val list = dataSource.getRemotePlaces(
                            "${it.lat},${it.lng}",
                            it.radius.toString(),
                            it.language
                        )
                        showLoading.postValue(false)
                        places.value = list.asItem()
                    } catch (ex: Exception) {
                        showSnackBar.value = ex.message
                    }
                    invalidateShowNoData()
                }
            }
        } catch (ex: Exception) {
            showLoading.value = false
            showSnackBar.value = ex.message
        }
    }

    private fun invalidateShowNoData() {
        showNoData.value = places.value == null || places.value!!.isEmpty()
    }

    fun updateLatLng(lat: Double, lng: Double) {
        this.lat = lat
        this.lng = lng
    }

    data class QueryItems(
        val lat: Double?,
        val lng: Double?,
        val radius: Int?,
        val language: String = "en"
    ) {
        fun checkValid(): QueryItems? =
            if (lat != null && lng != null && radius != null) this else null
    }
}