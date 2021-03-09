package com.maicondcastro.findfood.network.repository

import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.database.dto.PlaceDto
import com.maicondcastro.findfood.network.PlacesRemoteDataSource
import com.maicondcastro.findfood.domain.models.Place
import com.maicondcastro.findfood.utils.asDatabase
import com.maicondcastro.findfood.utils.asDomain
import com.maicondcastro.findfood.network.PlacesApiService
import com.maicondcastro.findfood.network.parsePlacesJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class PlacesRemoteRepository(
    private val placeDao: PlaceDao,
    private val networkPlaces: PlacesApiService
) : PlacesRemoteDataSource {

    override suspend fun getRemotePlaces(
        location: String,
        radius: String,
        language: String
    ): List<Place> {
        var jsonPlaces: List<PlaceDto>
        withContext(Dispatchers.IO) {
            val places = networkPlaces.getPlacesAsync(location, radius).await()

            placeDao.deleteNotSaved()
            jsonPlaces = parsePlacesJsonResult(JSONObject(places)).asDatabase()
            val databasePlaces = placeDao.getPlaces()
            databasePlaces.forEach { place ->
                jsonPlaces.firstOrNull { p -> p.placeId == place.placeId }?.saved = place.saved
            }
            placeDao.insertAll(*jsonPlaces.toTypedArray())

        }
        return jsonPlaces.asDomain()
    }
}