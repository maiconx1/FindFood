package com.maicondcastro.findfood.places.models

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.maicondcastro.findfood.utils.Utils
import java.io.Serializable

data class PlaceItem(
    val placeId: String,
    val name: String?,
    val rating: Double?,
    val userRatingTotal: Int?,
    val vicinity: String?,
    val openNow: Boolean?,
    val lat: Double?,
    val lng: Double?,
) : Serializable {
    val grayOut: Int
        get() = if (openNow == false) View.VISIBLE else View.GONE
    val ratingString =
        if (rating != null && userRatingTotal != null) "$rating ($userRatingTotal)" else "-"
}