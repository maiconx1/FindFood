package com.maicondcastro.findfood.common

import androidx.test.core.app.ApplicationProvider
import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.domain.PlacesDataSource
import com.maicondcastro.findfood.domain.repository.PlacesRepository
import com.maicondcastro.findfood.places.savedplaces.SavedPlacesViewModel
import org.junit.After
import org.junit.Before
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

interface BaseUnitTest : KoinTest {

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

            single<PlaceDao> { FakeDao() }

            single<PlacesDataSource> { PlacesRepository(get()) }
            single { PlacesRepository(get()) }
        }

        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(listOf(mainModule))
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}