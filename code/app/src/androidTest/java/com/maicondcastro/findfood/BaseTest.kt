package com.maicondcastro.findfood

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.maicondcastro.findfood.database.FakeDao
import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.database.PlaceDatabase
import com.maicondcastro.findfood.domain.PlacesDataSource
import com.maicondcastro.findfood.domain.repository.PlacesRepository
import com.maicondcastro.findfood.network.PlacesApiService
import com.maicondcastro.findfood.network.PlacesHttpClient
import com.maicondcastro.findfood.network.PlacesRemoteDataSource
import com.maicondcastro.findfood.network.repository.PlacesRemoteRepository
import com.maicondcastro.findfood.places.placelist.PlaceListViewModel
import com.maicondcastro.findfood.places.savedplaces.SavedPlacesViewModel
import com.maicondcastro.findfood.utils.Constants
import org.junit.After
import org.junit.Before
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

interface BaseTest : KoinTest {

    @Before
    fun init() {
        stopKoin()
        val mainModule = module {
            viewModel {
                SavedPlacesViewModel(
                    get(),
                    get() as PlacesDataSource
                )
            }

            viewModel {
                PlaceListViewModel(
                    get(),
                    get() as PlacesRemoteDataSource
                )
            }

            single(named("RealDao")) {
                Room.inMemoryDatabaseBuilder(
                    androidContext(),
                    PlaceDatabase::class.java
                ).build().placeDao }

            single<PlaceDao>(named("FakeDao")) { FakeDao() }
            single<PlacesDataSource> { PlacesRepository(get(named("RealDao"))) }
            //single { PlacesRepository(get(named("RealDao"))) }
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
                    .baseUrl(Constants.BASE_URL)
                    .client(get())
                    .addConverterFactory(get(named("scalars")))
                    .addConverterFactory(get(named("moshi")))
                    .addCallAdapterFactory(get())
                    .build()
            }

            single { get<Retrofit>().create(PlacesApiService::class.java) }

            single<PlacesRemoteDataSource> { PlacesRemoteRepository(get(named("RealDao")), get()) }
        }

        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(listOf(mainModule, networkModule))
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}