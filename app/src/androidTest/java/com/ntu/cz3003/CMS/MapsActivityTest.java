package com.ntu.cz3003.CMS;

import com.ntu.cz3003.CMS.ui.MapsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MapsActivityTest {
    @Rule
    public ActivityTestRule<MapsActivity> activityRule =
            new ActivityTestRule<>(MapsActivity.class);

    @Test
    public void clickMyLocationButton() {
        onView(withId(R.id.myLocationButton)).perform(click())
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void clickSubmitIncident() {
        onView(withId(R.id.toggleBottomSheetButton)).perform(click())
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void submitIncidents() {
        onView(withId(R.id.titleInput)).perform(typeText("Unit Test Case"))
                .check(matches(ViewMatchers.withText("Unit Test Case")));
        onView(withId(R.id.typeCategory)).perform(typeText("Flood"))
                .check(matches(ViewMatchers.withText("Flood")));
        onView(withId(R.id.descriptionInput)).perform(typeText("Testing Testing"))
                .check(matches(withText("Testing Testing")));
        onView(withId(R.id.submitRequestButton)).perform(click()).check(matches(isDisplayed()));
    }

}
