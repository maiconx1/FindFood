package com.maicondcastro.findfood.network

import androidx.lifecycle.LiveData
import com.maicondcastro.findfood.domain.models.Place

interface PlacesRemoteDataSource {

    val places: LiveData<List<Place>>

    suspend fun getRemotePlaces(
        location: String,
        radius: String,
        language: String = "en",
        types: String = "",
        name: String = ""
    ): List<Place>
}