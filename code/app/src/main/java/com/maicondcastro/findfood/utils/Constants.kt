package com.maicondcastro.findfood.utils

class Constants {
    companion object {
        const val PLACE_BUSINESS_OPERATIONAL = "OPERATIONAL"
        const val PLACE_BUSINESS_CLOSED_TEMPORARILY = "CLOSED_TEMPORARILY"
        const val PLACE_BUSINESS_CLOSED_PERMANENTLY = "CLOSED_PERMANENTLY"

        const val BASE_URL = "https://maps.googleapis.com/maps/api/place/"
        const val DEFAULT_MAX_DISTANCE = 500
    }
}