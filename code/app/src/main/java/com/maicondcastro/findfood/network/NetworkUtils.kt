package com.maicondcastro.findfood.network

import com.maicondcastro.findfood.extensions.*
import com.maicondcastro.findfood.domain.models.Place
import org.json.JSONObject

fun parsePlacesJsonResult(jsonResult: JSONObject): ArrayList<Place> {
    val resultsJson = jsonResult.getJSONArray("results")

    val placeList = ArrayList<Place>()

    for (i in 0 until resultsJson.length()) {
        val placeJson = resultsJson.getJSONObject(i)
        val placeId = placeJson.getStringOrNull("place_id") ?: continue // Shouldn't add to list if there is no place_id
        val name = placeJson.getStringOrNull("name")
        val rating = placeJson.getDoubleOrNull("rating")
        val lat = placeJson.getJSONObjectOrNull("geometry")?.getJSONObject("location")
            ?.getDoubleOrNull("lat")
        val userRatingTotal = placeJson.getIntOrNull("user_ratings_total")
        val vicinity = placeJson.getStringOrNull("vicinity")
        val openNow = placeJson.getJSONObjectOrNull("opening_hours")?.getBooleanOrNull("open_now")
        val lng = placeJson.getJSONObjectOrNull("geometry")?.getJSONObject("location")
            ?.getDoubleOrNull("lng")
        val businessStatus = placeJson.getStringOrNull("business_status")


        val place = Place(
            placeId,
            name,
            rating,
            userRatingTotal,
            vicinity,
            openNow,
            lat,
            lng,
            businessStatus
        )

        placeList.add(place)
    }

    return placeList
}