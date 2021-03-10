package com.maicondcastro.findfood.network.repository

import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.database.dto.PlaceDto
import com.maicondcastro.findfood.network.PlacesRemoteDataSource
import com.maicondcastro.findfood.domain.models.Place
import com.maicondcastro.findfood.utils.asDatabase
import com.maicondcastro.findfood.utils.asDomain
import com.maicondcastro.findfood.network.PlacesApiService
import com.maicondcastro.findfood.network.parsePlaceDetailsJsonResult
import com.maicondcastro.findfood.network.parsePlacesJsonResult
import com.maicondcastro.findfood.domain.models.PlaceDetail
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
            val places = networkPlaces.getNearbyPlacesAsync(location, radius, language).await()

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

    override suspend fun getPlaceDetails(
        placeId: String,
        language: String
    ): PlaceDetail? {
        var json: PlaceDetail?
        withContext(Dispatchers.IO) {
            val places = networkPlaces.getPlaceDetailAsync(placeId, language).await()
            json = parsePlaceDetailsJsonResult(JSONObject(places))
        }
        return json
    }
}