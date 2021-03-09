package com.maicondcastro.findfood.places

import com.maicondcastro.findfood.database.dto.PlaceDto

object PlaceTestHelper {
    const val LATITUDE: Double = 52.37606786972115
    const val LONGITUDE = 4.881061134642686
    val LOCATION = "$LATITUDE,$LONGITUDE"
    val PLACE_DTO_SAVED = PlaceDto("ChIJFfyzTTeuEmsRuMxvFyNRfbk", "name", 0.0, 0, "vicinity", true, LATITUDE, LONGITUDE, "OPERATIONAL", true)
    val PLACE_DTO = PlaceDto("ChIJFfyzTTeuEmsRuMxvFyNRfbk", "name", 0.0, 0, "vicinity", true, LATITUDE, LONGITUDE, "OPERATIONAL", false)
}