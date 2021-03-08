package com.maicondcastro.findfood.domain.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.maicondcastro.findfood.BaseTest
import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.domain.PlacesDataSource
import com.maicondcastro.findfood.getOrAwaitValue
import com.maicondcastro.findfood.network.repository.PlaceTestHelper.PLACE_DTO
import com.maicondcastro.findfood.network.repository.PlaceTestHelper.PLACE_DTO_SAVED
import com.maicondcastro.findfood.utils.asDomain
import com.maicondcastro.findfood.utils.exceptions.PlaceNotFoundException
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test
import org.koin.test.inject

class PlacesRepositoryTest : BaseTest {

    private val repository: PlacesDataSource by inject()

    private val dao: PlaceDao by inject()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun savePlace_success() {
        runBlocking {
            val place = PLACE_DTO
            dao.insertAll(place)

            val before = dao.getPlaceById(place.placeId)

            repository.savePlace(place.placeId, true)

            val after = dao.getPlaceById(place.placeId)

            assertThat(before?.saved, not(after?.saved))
        }
    }

    @Test(expected = PlaceNotFoundException::class)
    fun savePlace_throwPlaceNotFoundExceptionIfPlaceNotFound() {
        runBlocking {
            dao.insertAll(PLACE_DTO)

            repository.savePlace("random", true)
        }
    }

    @Test
    fun getSavedPlaces_success() {
        runBlocking {
            dao.insertAll(PLACE_DTO_SAVED)

            val value = repository.getSavedPlaces()

            assertThat(value, `is`(listOf(PLACE_DTO_SAVED).asDomain()))
        }
    }

    @Test
    fun getSavedPlaces_failed() {
        runBlocking {
            dao.insertAll(PLACE_DTO)

            val value = repository.getSavedPlaces()

            assertThat(value.isEmpty(), `is`(true))
        }
    }
}