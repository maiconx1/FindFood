package com.maicondcastro.findfood.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.maicondcastro.findfood.database.dto.PlaceDto

@Dao
interface PlaceDao {
    @Insert(onConflict = REPLACE)
    fun insertAll(vararg place: PlaceDto)

    @Update
    fun update(vararg place: PlaceDto)

    @Query("SELECT * FROM place WHERE placeId == :placeId")
    fun getPlaceById(placeId: String): PlaceDto?

    @Query("DELETE FROM place WHERE NOT saved")
    fun deleteNotSaved()

    @Query("SELECT * FROM place")
    fun getPlacesLiveData(): LiveData<List<PlaceDto>>

    @Query("SELECT * FROM place")
    fun getPlaces(): List<PlaceDto>

    @Query("SELECT * FROM place WHERE saved")
    fun getSavedPlacesLiveData(): LiveData<List<PlaceDto>>

    @Query("SELECT * FROM place WHERE saved")
    fun getSavedPlaces(): List<PlaceDto>
}