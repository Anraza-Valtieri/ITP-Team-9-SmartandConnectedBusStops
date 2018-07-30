package com.sit.itp_team_9_smartandconnectedbusstops;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.opencensus.stats.View;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class GetStartedActivityTest {

    @Rule
    public ActivityTestRule<GetStartedActivity> mActivityTestRule = new ActivityTestRule<GetStartedActivity>(GetStartedActivity.class);

    private GetStartedActivity mActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {

        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testLaunchMainActivity() {

        assertNotNull(mActivity.findViewById(R.id.nextBtn));

        onView(withId(R.id.nextBtn)).perform(click()).perform(click()).perform(click()).perform(click()).perform(click()).perform(click()).perform(click()).perform(click()).perform(click()).perform(click());

        Activity mainActivity = getInstrumentation().waitForMonitor(monitor);

        assertNotNull(mainActivity);

        mainActivity.finish();
    }

    @After
    public void tearDown() throws Exception {

        mActivity = null;
    }
}