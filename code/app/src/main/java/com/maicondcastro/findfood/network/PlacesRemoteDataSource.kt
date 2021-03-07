package com.maicondcastro.findfood.network

import com.maicondcastro.findfood.domain.models.Place

interface PlacesRemoteDataSource {
    suspend fun getRemotePlaces(
        location: String,
        radius: String,
        language: String = "en",
        types: String = "",
        name: String = ""
    ): List<Place>
}