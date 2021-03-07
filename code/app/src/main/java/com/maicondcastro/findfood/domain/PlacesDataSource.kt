package com.maicondcastro.findfood.domain

import androidx.lifecycle.LiveData
import com.maicondcastro.findfood.domain.models.Place

interface PlacesDataSource {

    val savedPlaces: LiveData<List<Place>>

    suspend fun savePlace(placeId: String, saved: Boolean)
}