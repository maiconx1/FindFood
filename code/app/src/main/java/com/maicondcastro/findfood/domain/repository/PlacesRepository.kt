package com.maicondcastro.findfood.domain.repository

import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.domain.PlacesDataSource
import com.maicondcastro.findfood.domain.models.Place
import com.maicondcastro.findfood.extensions.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlacesRepository(private val placeDao: PlaceDao): PlacesDataSource {
    override suspend fun updatePlace(place: Place) {
        withContext(Dispatchers.IO) {
            placeDao.update(place.asDatabaseModel())
        }
    }
}