package com.maicondcastro.findfood.domain

import androidx.lifecycle.LiveData
import com.maicondcastro.findfood.domain.models.Place

interface PlacesDataSource {

    suspend fun getSavedPlaces() : LiveData<List<Place>>

    suspend fun savePlace(placeId: String, saved: Boolean)
}