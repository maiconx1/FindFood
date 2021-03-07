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

    @Query("DELETE FROM place where not saved")
    fun deleteNotSaved()

    @Query("SELECT * FROM place")
    fun getPlacesLiveData(): LiveData<List<PlaceDto>>

    @Query("SELECT * FROM place")
    fun getPlaces(): List<PlaceDto>

    @Query("SELECT * FROM place where saved")
    fun getSavedPlacesLiveData(): LiveData<List<PlaceDto>>

    @Query("SELECT * FROM place where saved")
    fun getSavedPlaces(): List<PlaceDto>
}