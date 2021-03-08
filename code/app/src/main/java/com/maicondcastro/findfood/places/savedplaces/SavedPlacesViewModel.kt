package com.maicondcastro.findfood.places.savedplaces

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maicondcastro.findfood.base.BaseViewModel
import com.maicondcastro.findfood.places.PlaceItem
import com.maicondcastro.findfood.domain.PlacesDataSource
import com.maicondcastro.findfood.utils.asItemModel
import kotlinx.coroutines.launch

class SavedPlacesViewModel(
    app: Application,
    private val dataSource: PlacesDataSource
) : BaseViewModel(app) {

    var savedPlaces = MutableLiveData<List<PlaceItem>>()

    fun loadSavedPlaces() {
        showLoading.value = true
        viewModelScope.launch {
            try {
                //val list = MutableLiveData(listOf(Place("", null, null, null, null, null, null, null, null)))// dataSource.getSavedPlaces()
                //val l = listOf(PlaceItem("", "Teste", 4.0, null, "Teste", true))
                val list = dataSource.getSavedPlaces()
                showLoading.postValue(false)
                /*savedPlaces = Transformations.map(list) {
                    it.asItem()
                }*/
                savedPlaces.value = list.map { it.asItemModel() }
            } catch (ex: Exception) {
                showSnackBar.value = ex.message
            }
            invalidateShowNoData()
        }
    }

    private fun invalidateShowNoData() {
        showNoData.value = savedPlaces.value == null || savedPlaces.value!!.isEmpty()
    }
}