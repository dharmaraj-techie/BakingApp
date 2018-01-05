package com.techie.dharmaraj.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
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
public class FetchingDataTest {

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mainActivityActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }


    @Test
    public void checkData_CorrectlyLoadedToListTest(){
        //array recipe names
        String[] recipeNames = new String[]{"Nutella Pie","Brownies","Yellow Cake","Cheesecake"};
        //check all the 4 list item
        for(int i = 0;i<4;i++)
        Espresso.onData(CoreMatchers.anything())
                .inAdapterView(ViewMatchers.withId(R.id.grid_view))
                .atPosition(i)
                .onChildView(ViewMatchers.withId(R.id.recipe_name_tv))
                .check(ViewAssertions.matches(ViewMatchers.withText(recipeNames[i])));
    }

    @Test
    public void ClickAddToWidgetBtn_ShowsTextViewTest(){
            Espresso.onData(CoreMatchers.anything())
                    .inAdapterView(ViewMatchers.withId(R.id.grid_view))
                    .atPosition(0)
                    .perform(ViewActions.click());

            Espresso.onView(ViewMatchers.withId(R.id.steps_recycler_view))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));

            Espresso.onView(ViewMatchers.withId(R.id.add_to_widget_btn)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.added_to_widget_tv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }



    @After
    public void unRegisterIdlingResource() {
        if(mIdlingResource != null ){
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}
