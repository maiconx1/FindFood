package com.maicondcastro.findfood.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("json")
    fun getPlacesAsync(
            @Query("location") location: String,
            @Query("radius") radius: String,
            @Query("language") language: String = "en",
            @Query("types") types: String = "",
            @Query("name") name: String = ""
    ): Deferred<String>
}