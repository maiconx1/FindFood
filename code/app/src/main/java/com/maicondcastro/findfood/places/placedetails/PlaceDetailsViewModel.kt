package com.maicondcastro.findfood.places.placedetails

import android.app.Application
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.maicondcastro.findfood.base.BaseViewModel
import com.maicondcastro.findfood.base.NavigationCommand
import com.maicondcastro.findfood.domain.PlacesDataSource
import com.maicondcastro.findfood.network.PlacesRemoteDataSource
import com.maicondcastro.findfood.places.models.PlaceDetailItem
import com.maicondcastro.findfood.utils.SingleLiveEvent
import com.maicondcastro.findfood.utils.Utils.calculateDistance
import com.maicondcastro.findfood.utils.asDetailItemModel
import com.maicondcastro.findfood.utils.asItemModel
import kotlinx.coroutines.launch
import kotlin.math.pow

class PlaceDetailsViewModel(
    app: Application,
    private val remoteDataSource: PlacesRemoteDataSource,
    private val dataSource: PlacesDataSource,
) : BaseViewModel(app) {

    private var lat: Double? = null
    private var lng: Double? = null

    var placeItem = MutableLiveData<PlaceDetailItem>()

    val loadedPlace = MutableLiveData(false)

    private val _followButtonSaved = SingleLiveEvent<Boolean>()
    val followButtonSaved: LiveData<Boolean>
        get() = _followButtonSaved

    var placeId = ""

    private val _distance = MutableLiveData(-1.0)
    val distanceString: LiveData<String> = Transformations.map(_distance) {
        if (it == null || it < 0) ""
        else "${String.format("%.2f", it)} m"
    }

    fun loadDetails() {
        showLoading.value = true
        viewModelScope.launch {
            try {
                dataSource.getPlaceById(placeId)?.let {
                    placeItem.value = it.asDetailItemModel()
                    _followButtonSaved.value = it.saved
                }

                val place = remoteDataSource.getPlaceDetails(placeId)
                showLoading.postValue(false)
                placeItem.value = place?.asItemModel()
                loadedPlace.value = true
                if (lat != null && lng != null) {
                    _distance.value = calculateDistance(lat!!, lng!!, placeItem.value?.lat!!,placeItem.value?.lng!!)
                }
            } catch (ex: Exception) {
                showSnackBar.value = ex.message
                navigationCommand.value = NavigationCommand.Back
            }
        }
    }

    fun updateLatLng(lat: Double, lng: Double) {
        this.lat = lat
        this.lng = lng

        if (placeItem.value != null) {
            _distance.value = calculateDistance(lat, lng, placeItem.value?.lat!!,placeItem.value?.lng!!)
        }
    }

    fun savePlace() {
        viewModelScope.launch {
            dataSource.getPlaceById(placeId)?.let {
                dataSource.savePlace(placeId, !it.saved)
                _followButtonSaved.value = !it.saved
            }
        }
    }
}