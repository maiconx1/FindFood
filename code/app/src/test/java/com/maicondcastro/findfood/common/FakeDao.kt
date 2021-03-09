package com.maicondcastro.findfood.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.database.dto.PlaceDto
import kotlinx.coroutines.runBlocking

class FakeDao : PlaceDao {

    private val placeList = mutableListOf<PlaceDto>()

    var shouldReturnError = false

    override suspend fun insertAll(vararg place: PlaceDto) {
        placeList.addAll(place)
    }

    override suspend fun update(vararg place: PlaceDto) {
        place.forEach {
            placeList[placeList.indexOf(it)] = it
        }
    }

    override suspend fun getPlaceById(placeId: String): PlaceDto? {
        return if(!shouldReturnError) {
            placeList.firstOrNull { it.placeId == placeId }
        } else {
            throw Exception("error")
        }
    }

    override suspend fun deleteNotSaved() {
        placeList.removeIf { !it.saved }
    }

    override suspend fun getPlaces(): List<PlaceDto> {
        return if (!shouldReturnError) {
            placeList
        } else {
            throw Exception("error")
        }
    }

    override suspend fun getSavedPlaces(): List<PlaceDto> {
        return if(!shouldReturnError) {
            placeList.filter { it.saved }
        } else {
            throw Exception("error")
        }
    }
}