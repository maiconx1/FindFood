package com.maicondcastro.findfood.domain

import com.maicondcastro.findfood.domain.models.Place

interface PlacesDataSource {
    suspend fun updatePlace(place: Place)
}