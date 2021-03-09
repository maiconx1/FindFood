package com.maicondcastro.findfood.places.savedplaces

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.maicondcastro.findfood.BaseTest
import com.maicondcastro.findfood.R
import com.maicondcastro.findfood.database.PlaceDao
import com.maicondcastro.findfood.network.repository.PlaceTestHelper.PLACE_DTO_SAVED
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.qualifier.named
import org.koin.test.inject
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@MediumTest
class SavedPlacesFragmentTest : BaseTest {

    private val dao: PlaceDao by inject(named("RealDao"))

    @Test
    fun noData_DisplayedInUi() {
        launchFragmentInContainer<SavedPlacesFragment>(bundleOf(), R.style.Theme_FindFood)

        sleep(1000)
        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun savedList_DisplayedInUi() {
        runBlocking {
            val place = PLACE_DTO_SAVED
            dao.insertAll(place)

            launchFragmentInContainer<SavedPlacesFragment>(bundleOf(), R.style.Theme_FindFood)

            onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))

            onView(withId(R.id.saved_list)).check(matches(hasDescendant(withText(place.name))))
            onView(withId(R.id.saved_list)).check(matches(hasDescendant(withText(place.vicinity))))
            onView(withId(R.id.saved_list)).check(matches(hasDescendant(withText(place.rating.toString()))))
        }
    }

    @Test
    fun clickFindFood_NavigateToPlaceList() {
        val scenario =
            launchFragmentInContainer<SavedPlacesFragment>(bundleOf(), R.style.Theme_FindFood)
        val navController = mock(NavController::class.java)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.find_places_button)).perform(click())

        verify(navController).navigate(SavedPlacesFragmentDirections.actionSavedPlacesFragmentToPlaceListFragment())
    }

    @Test
    fun listItemClicked_NavigateToPlaceDetails() {
        runBlocking {
            val place = PLACE_DTO_SAVED
            dao.insertAll(place)

            val scenario =
                launchFragmentInContainer<SavedPlacesFragment>(bundleOf(), R.style.Theme_FindFood)
            val navController = mock(NavController::class.java)

            scenario.onFragment {
                Navigation.setViewNavController(it.view!!, navController)
            }

            onView(withText(place.name)).perform(click())

            // TODO: verify(navController).navigate()
        }
    }
}