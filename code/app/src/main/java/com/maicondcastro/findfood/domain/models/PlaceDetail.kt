package com.maicondcastro.findfood.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceDetail (
    val placeId: String,
    val name: String?,
    val rating: Double?,
    val userRatingTotal: Int?,
    val vicinity: String?,
    val openNow: Boolean?,
    val lat: Double?,
    val lng: Double?,
    val businessStatus: String?,
    val icon: String?,
    val weekdayText: List<String>?,
    val url: String?,
    val webSite: String?
) : Parcelable