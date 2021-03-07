package com.maicondcastro.findfood.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.domain.PlacesDataSource
import com.maicondcastro.findfood.domain.models.Place
import com.maicondcastro.findfood.utils.exceptions.PlaceNotFoundException
import com.maicondcastro.findfood.utils.extensions.asDatabaseModel
import com.maicondcastro.findfood.utils.extensions.asDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class PlacesRepository(private val placeDao: PlaceDao) : PlacesDataSource {


    override suspend fun getSavedPlaces(): LiveData<List<Place>> =
        Transformations.map(placeDao.getSavedPlacesLiveData()) {
            it.asDomain()
        }

    override suspend fun savePlace(placeId: String, saved: Boolean) {
        withContext(Dispatchers.IO) {
            placeDao.getPlaceById(placeId)?.let {
                it.saved = true
                placeDao.update(it)
            } ?: throw PlaceNotFoundException()
        }
    }
}