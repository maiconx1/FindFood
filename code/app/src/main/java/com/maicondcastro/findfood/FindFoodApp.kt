package com.maicondcastro.findfood

import android.app.Application
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.maicondcastro.findfood.Constants.Companion.BASE_URL
import com.maicondcastro.findfood.domain.repository.PlacesRepository
import com.maicondcastro.findfood.network.PlacesApiService
import com.maicondcastro.findfood.network.PlacesHttpClient
import com.maicondcastro.findfood.network.repository.PlacesRemoteRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class FindFoodApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val networkModule = module {
            single {
                PlacesHttpClient.getClient()
            }

            single {
                ScalarsConverterFactory.create()
            }

            single {
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            }

            single {
                CoroutineCallAdapterFactory()
            }

            single {
                Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(get())
                    .addConverterFactory(get())
                    .addConverterFactory(get())
                    .addCallAdapterFactory(get())
                    .build()
            }

            single { get<Retrofit>().create(PlacesApiService::class.java) }

            single { PlacesRemoteRepository(get(), get()) }
            single { PlacesRepository(get()) }
        }

        startKoin {
            androidContext(this@FindFoodApp)
            modules(listOf(networkModule))
        }
    }
}