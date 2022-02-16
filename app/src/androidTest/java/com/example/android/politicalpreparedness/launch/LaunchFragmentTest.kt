package com.example.android.politicalpreparedness.launch

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.politicalpreparedness.R
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`

@RunWith(AndroidJUnit4::class)
class LaunchFragmentTest {

    @Test
    fun launchFragment_displayedInUi() {
        launchFragmentInContainer<LaunchFragment>()

        onView(withContentDescription(R.string.content_desc_ballot_logo)).check(matches(isDisplayed()))
        onView(withText(R.string.button_text_upcoming_elections)).check(matches(isDisplayed()))
        onView(withText(R.string.button_text_find_my_representatives)).check(matches(isDisplayed()))
    }

    @Test
    fun clickUpcomingElectionsButton_navigateToElectionsScreen() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        val scenario = launchFragmentInContainer<LaunchFragment>()
        scenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withText(R.string.button_text_upcoming_elections)).perform(click())
        assertThat(navController.currentDestination?.id, `is`(R.id.electionsFragment))
    }
}