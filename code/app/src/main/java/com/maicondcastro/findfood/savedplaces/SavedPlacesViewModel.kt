package com.maicondcastro.findfood.savedplaces

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.maicondcastro.findfood.base.BaseViewModel
import com.maicondcastro.findfood.domain.PlacesDataSource
import com.maicondcastro.findfood.domain.models.Place
import kotlinx.coroutines.launch

class SavedPlacesViewModel(
    app: Application,
    private val dataSource: PlacesDataSource
) : BaseViewModel(app) {

    var savedPlaces: LiveData<List<Place>> = MutableLiveData(listOf())

    fun loadSavedPlaces() {
        showLoading.value = true
        viewModelScope.launch {
            try {
                val list = dataSource.getSavedPlaces()
                showLoading.postValue(false)
                savedPlaces = Transformations.map(list) {
                    it
                }
            } catch (ex: Exception) {
                showSnackBar.value = ex.message
            }
        }
        invalidateShowNoData()
    }

    private fun invalidateShowNoData() {
        showNoData = Transformations.map(savedPlaces) {
            it.isEmpty()
        }
        //showNoData.value = savedPlaces.value == null || savedPlaces.value!!.isEmpty()
    }
}