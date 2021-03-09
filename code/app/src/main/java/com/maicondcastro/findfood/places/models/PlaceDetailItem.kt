package com.maicondcastro.findfood.places.models

import android.view.View
import com.maicondcastro.findfood.R
import java.io.Serializable
import java.lang.StringBuilder


class PlaceDetailItem(
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
) : Serializable {
    val ratingString =
        if (rating != null && userRatingTotal != null) "$rating ($userRatingTotal)" else "-"
    val openNowText = if (openNow == true) R.string.open_now else R.string.closed
    val hasWebSite = !webSite.isNullOrEmpty()
    val hasIcon = !icon.isNullOrEmpty()
    val hasMapUrl = !url.isNullOrEmpty()
    val weekString = StringBuilder().apply {
        weekdayText?.forEachIndexed { index, string ->
            if (index > 0) append("\n")
            append(string)
        }
    }
}