package com.techie.dharmaraj.bakingapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.techie.dharmaraj.bakingapp.utils.NetworkUtils;

/**
 * Created by Dharmaraj on 10-11-2017.
 */
public class RecipeLoader extends AsyncTaskLoader<String> {

    private SimpleIdlingResource mIdlingResource;
    public RecipeLoader(Context context, SimpleIdlingResource idlingResource) {
        super(context);
        mIdlingResource = idlingResource;
    }

    @Override
    public String loadInBackground() {
        mIdlingResource.setIdleState(false);
        return NetworkUtils.fetchData();
    }
}
