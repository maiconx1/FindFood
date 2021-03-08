package com.maicondcastro.findfood

import android.app.Application
import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.maicondcastro.findfood.database.PlaceDatabase
import com.maicondcastro.findfood.domain.PlacesDataSource
import com.maicondcastro.findfood.utils.Constants.Companion.BASE_URL
import com.maicondcastro.findfood.domain.repository.PlacesRepository
import com.maicondcastro.findfood.network.PlacesApiService
import com.maicondcastro.findfood.network.PlacesHttpClient
import com.maicondcastro.findfood.network.PlacesRemoteDataSource
import com.maicondcastro.findfood.network.repository.PlacesRemoteRepository
import com.maicondcastro.findfood.places.savedplaces.SavedPlacesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class FindFoodApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val mainModule = module {
            viewModel {
                SavedPlacesViewModel(
                    get(),
                    get() as PlacesDataSource
                )
            }


            single {
                Room.databaseBuilder(
                    applicationContext,
                    PlaceDatabase::class.java,
                    "place_database.db"
                ).build().placeDao
            }

            single<PlacesDataSource> { PlacesRepository(get()) }
            single { PlacesRepository(get()) }
        }

        val networkModule = module {
            single {
                PlacesHttpClient.getClient()
            }

            single<Converter.Factory>(named("scalars")) {
                ScalarsConverterFactory.create()
            }

            single<Converter.Factory>(named("moshi")) {
                MoshiConverterFactory.create()
            }

            single<CallAdapter.Factory> {
                CoroutineCallAdapterFactory()
            }

            single {
                Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(get())
                    .addConverterFactory(get(named("scalars")))
                    .addConverterFactory(get(named("moshi")))
                    .addCallAdapterFactory(get())
                    .build()
            }

            single { get<Retrofit>().create(PlacesApiService::class.java) }

            single<PlacesRemoteDataSource> { PlacesRemoteRepository(get(), get()) }
        }

        startKoin {
            androidContext(this@FindFoodApp)
            modules(listOf(mainModule, networkModule))
        }
    }
}