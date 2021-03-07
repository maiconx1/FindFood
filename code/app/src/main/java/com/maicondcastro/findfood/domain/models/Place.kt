package com.maicondcastro.findfood.domain.models

import android.os.Parcelable
import com.maicondcastro.findfood.utils.Constants
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Place(
    val placeId: String,
    val name: String?,
    val rating: Double?,
    val userRatingTotal: Int?,
    val vicinity: String?,
    val openNow: Boolean?,
    val lat: Double?,
    val lng: Double?,
    val businessStatus: String?
) : Parcelable {
    val shouldList: Boolean
        get() = businessStatus == Constants.PLACE_BUSINESS_OPERATIONAL
}