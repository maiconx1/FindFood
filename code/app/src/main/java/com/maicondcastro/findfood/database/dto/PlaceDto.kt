package com.maicondcastro.findfood.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place")
class PlaceDto constructor(
    @PrimaryKey
    val placeId: String?,
    val name: String?,
    val rating: Double?,
    val userRatingTotal: Int?,
    val vicinity: String?,
    val openNow: Boolean?,
    val lat: Double?,
    val lng: Double?,
    val businessStatus: String?,
    var saved: Boolean = false
)