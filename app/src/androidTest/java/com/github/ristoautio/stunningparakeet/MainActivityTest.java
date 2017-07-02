package com.github.ristoautio.stunningparakeet;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by ripa on 26.6.2017.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity_> menuActivityTestRule = new ActivityTestRule<>(MainActivity_.class, true, true);

    @Test
    public void testSomethig() throws InterruptedException {
        onView(withId(R.id.tvHello)).check(matches(withText("No file selected")));
        onView(withId(R.id.fab)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }
}
