package com.maicondcastro.findfood.savedplaces

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maicondcastro.findfood.common.BaseUnitTest
import com.maicondcastro.findfood.common.FakeDao
import com.maicondcastro.findfood.common.MainCoroutineRule
import com.maicondcastro.findfood.common.getOrAwaitValue
import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.domain.models.Place
import com.maicondcastro.findfood.savedplaces.PlaceTestHelper.PLACE_DTO_SAVED
import com.maicondcastro.findfood.utils.extensions.asDomainModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.notNullValue
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
        assertThat(viewModel.savedPlaces.value, `is`(Matchers.nullValue()))
        viewModel.loadSavedPlaces()
        assertThat(
            viewModel.savedPlaces.getOrAwaitValue(),
            `is`(Matchers.notNullValue())
        )
    }


    @Test
    fun loadSavedPlaces_successValues() {
        fakeDao.insertAll(PLACE_DTO_SAVED)

        assertThat(viewModel.savedPlaces.value, `is`(Matchers.nullValue()))
        viewModel.loadSavedPlaces()
        val places = listOf(PLACE_DTO_SAVED)
        val dataList = ArrayList<Place>()
        dataList.addAll(places.map { place ->
            place.asDomainModel()
        })
        assertEquals(viewModel.savedPlaces.getOrAwaitValue(), dataList)
    }



    @Test
    fun loadSavedPlaces_error() {
        (fakeDao as FakeDao).shouldReturnError = true

        assertThat(viewModel.showSnackBar.value, `is`(Matchers.nullValue()))
        viewModel.loadSavedPlaces()
        assertThat(
            viewModel.showSnackBar.getOrAwaitValue(),
            IsNot.not(`is`(Matchers.nullValue()))
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
        fakeDao.insertAll(PLACE_DTO_SAVED)
        viewModel.loadSavedPlaces()
        assertThat(viewModel.savedPlaces.getOrAwaitValue(), `is`(notNullValue()))
        assertThat(viewModel.showNoData.getOrAwaitValue(), `is`(false))
    }
}