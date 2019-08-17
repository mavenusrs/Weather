package com.mavenusrs.weather

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.mavenusrs.weather.presentation.weather.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers.*


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)


    @Before
    fun init(){
        activityRule.activity
            .supportFragmentManager.beginTransaction()
    }

    @Test
    fun testHappyPath(){
        onView(withId(R.id.loadingProgressBar)).check(matches(isDisplayed()))
        onView(withId(R.id.country)).check(matches(isDisplayed()))
        onView(withId(R.id.weatherDegreeTV)).check(matches(isDisplayed()))

    }



}