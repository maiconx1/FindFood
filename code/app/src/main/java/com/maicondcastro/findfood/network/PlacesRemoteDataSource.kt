package com.maicondcastro.findfood.network

import com.maicondcastro.findfood.domain.models.Place
import com.maicondcastro.findfood.domain.models.PlaceDetail

interface PlacesRemoteDataSource {

    suspend fun getRemotePlaces(
        location: String,
        radius: String,
        language: String = "en"
    ): List<Place>

    suspend fun getPlaceDetails(
        placeId: String,
        language: String = "en"
    ): PlaceDetail?
}