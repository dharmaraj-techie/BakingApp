package com.techie.dharmaraj.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.techie.dharmaraj.bakingapp.ui.MainActivity;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Dharmaraj on 03-12-2017.
 */
@RunWith(AndroidJUnit4.class)
public class IntentTesting {

    private IdlingResource mIdlingResource;

    @Rule
    public IntentsTestRule<MainActivity> mainActivityIntentsTestRule =
            new IntentsTestRule<MainActivity>(MainActivity.class);


    @Before
    public void registerIdlingResource() {
        mIdlingResource = mainActivityIntentsTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }


    @Before
    public void stubAllExternalIntent(){
        //block all external intents
        Intents.intending(CoreMatchers.not(IntentMatchers.isInternal()))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK,null));
    }

    @Test
    public void listItemClicked_LaunchesStepsActivityTest(){
        Espresso.onData(CoreMatchers.anything())
                .inAdapterView(ViewMatchers.withId(R.id.grid_view))
                .atPosition(0)
                .perform(ViewActions.click());

        Intents.intended(CoreMatchers.allOf(
                IntentMatchers.toPackage(getClass().getPackage().getName()),
                IntentMatchers.hasExtra("position",0)));
    }


    @After
    public void unRegisterIdlingResource() {
        if(mIdlingResource != null ){
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}
