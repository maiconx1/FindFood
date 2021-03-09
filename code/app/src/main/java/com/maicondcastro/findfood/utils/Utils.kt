package com.maicondcastro.findfood.utils

import android.location.Location

object Utils {
    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val loc1 = Location("")
        loc1.latitude = lat1
        loc1.longitude = lng1

        val loc2 = Location("")
        loc2.latitude = lat2
        loc2.longitude = lng2

        return loc1.distanceTo(loc2).toDouble()
    }
}