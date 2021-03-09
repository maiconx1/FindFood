package com.maicondcastro.findfood.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.maicondcastro.findfood.database.dto.PlaceDto

@Dao
interface PlaceDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertAll(vararg place: PlaceDto)

    @Update
    suspend fun update(vararg place: PlaceDto)

    @Query("SELECT * FROM place WHERE placeId == :placeId")
    suspend fun getPlaceById(placeId: String): PlaceDto?

    @Query("DELETE FROM place WHERE NOT saved")
    suspend fun deleteNotSaved()

    @Query("SELECT * FROM place")
    suspend fun getPlaces(): List<PlaceDto>

    @Query("SELECT * FROM place WHERE saved")
    suspend fun getSavedPlaces(): List<PlaceDto>
}