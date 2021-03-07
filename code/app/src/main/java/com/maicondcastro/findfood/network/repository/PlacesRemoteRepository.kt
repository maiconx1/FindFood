package com.maicondcastro.findfood.network.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.network.PlacesRemoteDataSource
import com.maicondcastro.findfood.domain.models.Place
import com.maicondcastro.findfood.extensions.asDatabase
import com.maicondcastro.findfood.extensions.asDomain
import com.maicondcastro.findfood.network.PlacesApiService
import com.maicondcastro.findfood.network.parsePlacesJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class PlacesRemoteRepository(
    private val placeDao: PlaceDao,
    private val networkPlaces: PlacesApiService
) : PlacesRemoteDataSource {

    val places: LiveData<List<Place>> = Transformations.map(placeDao.getPlacesLiveData()) {
        it.asDomain()
    }

    override suspend fun getRemotePlaces(
        location: String,
        radius: String,
        language: String,
        types: String,
        name: String
    ): List<Place> {
        withContext(Dispatchers.IO) {
            val places = networkPlaces.getPlacesAsync(location, radius).await()

            placeDao.deleteNotSaved()
            val jsonPlaces = parsePlacesJsonResult(JSONObject(places)).asDatabase()
            val databasePlaces = placeDao.getPlaces()
            databasePlaces.forEach { place ->
                if (databasePlaces.firstOrNull { p -> p.placeId == place.placeId } != null) {
                    place.saved = true
                }
            }
            placeDao.insertAll(*jsonPlaces.toTypedArray())
        }
        return listOf()
    }
}