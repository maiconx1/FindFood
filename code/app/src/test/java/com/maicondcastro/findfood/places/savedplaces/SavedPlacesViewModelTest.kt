package com.maicondcastro.findfood.places.savedplaces

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maicondcastro.findfood.common.*
import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.places.models.PlaceItem
import com.maicondcastro.findfood.places.PlaceTestHelper.PLACE_DTO_SAVED
import com.maicondcastro.findfood.utils.asDomainModel
import com.maicondcastro.findfood.utils.asItemModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.Matchers.nullValue
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O])
class SavedPlacesViewModelTest : BaseUnitTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val viewModel: SavedPlacesViewModel by inject()

    private val fakeDao: PlaceDao by inject()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun loadSavedPlaces_loading() {
        runBlockingTest {
            mainCoroutineRule.pauseDispatcher()
            viewModel.loadSavedPlaces()
            assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(true))
            mainCoroutineRule.resumeDispatcher()
            assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(false))
        }
    }

    @Test
    fun loadSavedPlaces_successList() {
        assertThat(viewModel.savedPlaces.value, `is`(nullValue()))
        viewModel.loadSavedPlaces()
        assertThat(
            viewModel.savedPlaces.getOrAwaitValue(),
            `is`(notNullValue())
        )
    }


    @Test
    fun loadSavedPlaces_successValues() {
        runBlockingTest {
            fakeDao.insertAll(PLACE_DTO_SAVED)

            assertThat(viewModel.savedPlaces.value, `is`(nullValue()))
            viewModel.loadSavedPlaces()
            val places = listOf(PLACE_DTO_SAVED)
            val dataList = ArrayList<PlaceItem>()
            dataList.addAll(places.map { place ->
                place.asDomainModel().asItemModel()
            })
            assertEquals(viewModel.savedPlaces.getOrAwaitValue(), dataList)
        }
    }


    @Test
    fun loadSavedPlaces_error() {
        (fakeDao as FakeDao).shouldReturnError = true

        assertThat(viewModel.showSnackBar.value, `is`(nullValue()))
        viewModel.loadSavedPlaces()
        assertThat(
            viewModel.showSnackBar.getOrAwaitValue(),
            IsNot.not(`is`(nullValue()))
        )
    }


    @Test
    fun invalidateShowNoData_noData() {
        (fakeDao as FakeDao).shouldReturnError = true
        viewModel.loadSavedPlaces()
        assertThat(viewModel.showNoData.getOrAwaitValue(), `is`(true))
    }

    @Test
    fun invalidateShowNoData_hasData() {
        runBlockingTest {
            fakeDao.insertAll(PLACE_DTO_SAVED)
            viewModel.loadSavedPlaces()
            assertThat(viewModel.savedPlaces.getOrAwaitValue(), `is`(notNullValue()))
            assertThat(viewModel.showNoData.getOrAwaitValue(), `is`(false))
        }
    }
}