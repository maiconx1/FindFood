package com.maicondcastro.findfood.domain.repository

import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.domain.PlacesDataSource
import com.maicondcastro.findfood.domain.models.Place
import com.maicondcastro.findfood.utils.asDomainModel
import com.maicondcastro.findfood.utils.exceptions.PlaceNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlacesRepository(private val placeDao: PlaceDao) : PlacesDataSource {


    override suspend fun getSavedPlaces(): List<Place> =
        placeDao.getSavedPlaces().map { it.asDomainModel() }

    override suspend fun getPlaceById(placeId: String): Place? =
        placeDao.getPlaceById(placeId)?.asDomainModel()

    override suspend fun savePlace(placeId: String, saved: Boolean) {
        withContext(Dispatchers.IO) {
            placeDao.getPlaceById(placeId)?.let {
                it.saved = saved
                placeDao.update(it)
            } ?: throw PlaceNotFoundException()
        }
    }
}