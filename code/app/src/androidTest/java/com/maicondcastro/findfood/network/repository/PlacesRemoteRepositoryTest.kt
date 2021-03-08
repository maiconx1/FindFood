package com.maicondcastro.findfood.network.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.maicondcastro.findfood.BaseTest
import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.getOrAwaitValue
import com.maicondcastro.findfood.network.PlacesRemoteDataSource
import com.maicondcastro.findfood.network.repository.PlaceTestHelper.PLACE_DTO_SAVED
import com.maicondcastro.findfood.utils.asDomainModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class PlacesRemoteRepositoryTest : BaseTest {

    private val repository: PlacesRemoteDataSource by inject()
    private val placeDao: PlaceDao by inject()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getRemotePlaces_success() {
        runBlocking {
            val before = placeDao.getPlaces()

            // Check if places list is empty before calling API
            assertThat(before.isEmpty(), `is`(true))

            repository.getRemotePlaces(location = "-33.8670522,151.1957362", radius = "500")

            val after = placeDao.getPlaces()

            // Check if places were added after calling API
            assertThat(after.isEmpty(), `is`(false))
        }
    }

    @Test
    fun getRemotePlaces_failure() {
        runBlocking {
            val before = placeDao.getPlaces()

            // Check if places list is empty before calling API
            assertThat(before.isEmpty(), `is`(true))

            repository.getRemotePlaces(location = "", radius = "")

            val after = placeDao.getPlaces()

            // Check if places list is empty after calling API fails
            assertThat(after.isEmpty(), `is`(true))
        }
    }

    @Test
    fun getRemotePlaces_successKeepSaved() {
        runBlocking {
            placeDao.insertAll(PLACE_DTO_SAVED.copy(placeId = "test"))
            val before = placeDao.getSavedPlaces()

            // Check if there is a saved place on DAO
            assertThat(before.isEmpty(), `is`(false))

            val savedPlace = before.first()

            repository.getRemotePlaces(location = "-33.8670522,151.1957362", radius = "500")

            val after = placeDao.getPlaces()

            // Check if saved place is still on the list
            val place = after.find { it.placeId == savedPlace.placeId }
            assertThat(place, `is`(notNullValue()))
        }
    }

    @Test
    fun getRemotePlaces_successKeepSavedOnConflict() {
        runBlocking {
            placeDao.insertAll(PLACE_DTO_SAVED)
            val before = placeDao.getSavedPlaces()

            // Check if there is a saved place on DAO
            assertThat(before.isEmpty(), `is`(false))

            val savedPlace = before.first()

            repository.getRemotePlaces(location = "-33.8670522,151.1957362", radius = "500")

            val after = placeDao.getPlaces()

            // Check if saved place is still on the list, was updated and keeps as saved
            val place = after.find { it.placeId == savedPlace.placeId }
            assertThat(place?.saved, `is`(true))
            assertThat(place?.name, not(savedPlace.name))
        }
    }

    @Test
    fun places_updatesLiveData() {
        placeDao.insertAll(PLACE_DTO_SAVED)

        val liveData = repository.places

        val value = liveData.getOrAwaitValue()

        assertThat(value, `is`(listOf(PLACE_DTO_SAVED.asDomainModel())))
    }
}