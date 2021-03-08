package com.maicondcastro.findfood.places

import android.view.View
import java.io.Serializable

data class PlaceItem(
    val placeId: String,
    val name: String?,
    val rating: Double?,
    val userRatingTotal: Int?,
    val vicinity: String?,
    val openNow: Boolean?
) : Serializable {
    val grayOut: Int
        get() = if (openNow == false) View.VISIBLE else View.GONE
}