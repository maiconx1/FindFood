package com.maicondcastro.findfood.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("nearbysearch/json")
    fun getNearbyPlacesAsync(
            @Query("location") location: String,
            @Query("radius") radius: String,
            @Query("language") language: String = "en",
            @Query("types") types: String = "bar|restaurant|cafe"
    ): Deferred<String>

    @GET("details/json")
    fun getPlaceDetailAsync(
            @Query("place_id") placeId: String
    ): Deferred<String>
}