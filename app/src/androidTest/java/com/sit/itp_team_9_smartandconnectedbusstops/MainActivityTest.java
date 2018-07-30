package com.sit.itp_team_9_smartandconnectedbusstops;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.support.design.internal.BottomNavigationItemView;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.view.Gravity;
import android.view.View;

import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertNotNull;

import org.hamcrest.Matcher;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.Matchers.not;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mActivity = null;

    Instrumentation.ActivityMonitor appguidemonitor = getInstrumentation().addMonitor(AppGuideActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor aboutappmonitor = getInstrumentation().addMonitor(AboutActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor datasourcesmonitor = getInstrumentation().addMonitor(DataSourcesActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {

        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testNavigationDrawer() {

        assertNotNull(mActivity.findViewById(R.id.drawer_layout));

        // Open Drawer to click on Navigation
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
    }

    @Test
    public void testDisplayWeather() {

        assertNotNull(mActivity.findViewById(R.id.drawer_layout));

        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        onView(withId(R.id.textView)).check(matches(withText(MainActivity.context.getResources().getString(R.string.current_weather))));
        onView(withId(R.id.tvLocation)).check(matches(not(withText(""))));
        onView(withId(R.id.tvWeather)).check(matches(withText(endsWith(")"))));
        onView(withId(R.id.tvTemperature)).check(matches(withText(endsWith(MainActivity.context.getResources().getString(R.string.degree)))));
        onView(withId(R.id.tvPSI25)).check(matches(withText(startsWith("PM2.5:"))));
        onView(withId(R.id.tvPSI10)).check(matches(withText(startsWith("PM10:"))));
        onView(withId(R.id.tvUV)).check(matches(withText(startsWith(MainActivity.context.getResources().getString(R.string.uvIndex)))));
    }

    @Test
    public void testChangeChinese() {

        assertNotNull(mActivity.findViewById(R.id.drawer_layout));

        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_language_preferences));

        onView(withText("中文")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
    }

    @Test
    public void testChangeMalay() {

        assertNotNull(mActivity.findViewById(R.id.drawer_layout));

        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_language_preferences));

        onView(withText("Bahasa Melayu")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
    }

//    @Test
//    public void testChangeTamil() {
//
//        assertNotNull(mActivity.findViewById(R.id.drawer_layout));
//
//        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
//
//        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_language_preferences));
//
//        onView(withText("தமிழ்")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
//    }

    @Test
    public void testStartAppGuideActivity() {

        assertNotNull(mActivity.findViewById(R.id.drawer_layout));

        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_app_guide));

        Activity appGuideActivity = getInstrumentation().waitForMonitor(appguidemonitor);

        assertNotNull(appGuideActivity);

        appGuideActivity.finish();
    }

    @Test
    public void testStartAboutActivity() {

        assertNotNull(mActivity.findViewById(R.id.drawer_layout));

        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_about_app));

        Activity aboutActivity = getInstrumentation().waitForMonitor(aboutappmonitor);

        assertNotNull(aboutActivity);

        aboutActivity.finish();
    }

    @Test
    public void testStartDataSourcesActivity() {

        assertNotNull(mActivity.findViewById(R.id.drawer_layout));

        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_datasources));

        Activity datasourcesActivity = getInstrumentation().waitForMonitor(datasourcesmonitor);

        assertNotNull(datasourcesActivity);

        datasourcesActivity.finish();
    }

    @Test
    public void testFeedbackOpenBrowser() {

        assertNotNull(mActivity.findViewById(R.id.drawer_layout));

        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        Intents.init();
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_VIEW), hasData(Uri.parse("https://goo.gl/forms/EgthF6mMFOLt6vci1")));
        intending(expectedIntent).respondWith(new Instrumentation.ActivityResult(0, null));
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_feedback));
        intended(expectedIntent);
        Intents.release();
    }

    /*@Test
    public void testBottomNavigationBar() {
        onView(withId(R.id.bottom_navigation))
                .check(matches(isDisplayed()));

        onView(withId(R.id.action_nearby)).check(matches(withBottomNavItemCheckedStatus(true)));
    }

    @After
    public void tearDown() throws Exception {

        mActivity = null;
    }*/

    public static Matcher<View> withBottomNavItemCheckedStatus(final boolean isChecked) {
        return new BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView.class) {
            boolean triedMatching;

            @Override
            public void describeTo(Description description) {
                if (triedMatching) {
                    description.appendText("with BottomNavigationItem check status: " + String.valueOf(isChecked));
                    description.appendText("But was: " + String.valueOf(!isChecked));
                }
            }

            @Override
            protected boolean matchesSafely(BottomNavigationItemView item) {
                triedMatching = true;
                return item.getItemData().isChecked() == isChecked;
            }
        };
    }
}