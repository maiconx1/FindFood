package com.maicondcastro.findfood.network.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.maicondcastro.findfood.BaseTest
import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.domain.PlacesDataSource
import com.maicondcastro.findfood.network.PlacesRemoteDataSource
import com.maicondcastro.findfood.network.repository.PlaceTestHelper.LOCATION
import com.maicondcastro.findfood.network.repository.PlaceTestHelper.PLACE_DTO_SAVED
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.qualifier.named
import org.koin.test.inject

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class PlacesRemoteRepositoryTest : BaseTest {

    private val remoteRepository: PlacesRemoteDataSource by inject()
    private val localRepository: PlacesDataSource by inject()
    private val placeDao: PlaceDao by inject(named("RealDao"))

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getRemotePlaces_success() {
        runBlocking {
            val before = placeDao.getPlaces()

            // Check if places list is empty before calling API
            assertThat(before.isEmpty(), `is`(true))

            remoteRepository.getRemotePlaces(location = LOCATION, radius = "500")

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

            remoteRepository.getRemotePlaces(location = "", radius = "")

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

            remoteRepository.getRemotePlaces(location = LOCATION, radius = "500")

            val after = placeDao.getPlaces()

            // Check if saved place is still on the list
            val place = after.find { it.placeId == savedPlace.placeId }
            assertThat(place, `is`(notNullValue()))
        }
    }

    @Test
    fun getRemotePlaces_successKeepSavedOnConflict() {
        runBlocking {
            remoteRepository.getRemotePlaces(location = LOCATION, radius = "500")

            var before = placeDao.getPlaces()

            localRepository.savePlace(before.first().placeId, true)

            before = placeDao.getSavedPlaces()

            // Check if there is a saved place on DAO
            assertThat(before.none { it.saved }, `is`(false))

            val savedPlace = before.first()

            remoteRepository.getRemotePlaces(location = LOCATION, radius = "500")

            val after = placeDao.getPlaces()

            // Check if saved place is still on the list, was updated and keeps as saved
            val place = after.find { it.placeId == savedPlace.placeId }
            assertThat(place?.saved, `is`(true))
            assertThat(place?.name, `is`(savedPlace.name))
        }
    }

    @Test
    fun places_updatesLiveData() {
        runBlocking {
            val value = remoteRepository.getRemotePlaces(
                location = LOCATION,
                radius = "500"
            )

            assertThat(value.isEmpty(), `is`(false))
        }
    }
}