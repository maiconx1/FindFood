package com.maicondcastro.findfood.network

import com.maicondcastro.findfood.BuildConfig
import okhttp3.OkHttpClient

class PlacesHttpClient {
    companion object {

        private const val API_KEY = BuildConfig.PLACES_API_KEY

        fun getClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val url = original
                        .url()
                        .newBuilder()
                        .addQueryParameter("key", API_KEY)
                        .build()
                    val request = original
                        .newBuilder()
                        .url(url)
                        .build()
                    chain.proceed(request)
                }
                .build()
        }

    }
}