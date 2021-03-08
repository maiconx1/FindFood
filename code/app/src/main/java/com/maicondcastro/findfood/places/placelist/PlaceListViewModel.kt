package com.maicondcastro.findfood.places.placelist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maicondcastro.findfood.base.BaseViewModel
import com.maicondcastro.findfood.domain.models.Place
import com.maicondcastro.findfood.network.PlacesRemoteDataSource
import kotlinx.coroutines.launch

class PlaceListViewModel(
    app: Application,
    private val dataSource: PlacesRemoteDataSource
) : BaseViewModel(app) {

    var places = MutableLiveData<List<Place>>(listOf())

    fun loadPlaces(lat: Double, lng: Double, radius: Int) {
        showLoading.value = true
        viewModelScope.launch {
            try {
                val list = dataSource.getRemotePlaces("$lat,$lng", radius.toString())
                showLoading.postValue(false)
                places.value = list
            } catch (ex: Exception) {
                showSnackBar.value = ex.message
            }
        }
        invalidateShowNoData()
    }

    private fun invalidateShowNoData() {
        showNoData.value = places.value == null || places.value!!.isEmpty()
    }
}