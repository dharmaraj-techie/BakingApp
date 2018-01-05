package com.techie.dharmaraj.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.techie.dharmaraj.bakingapp.R;
import com.techie.dharmaraj.bakingapp.RecipeLoader;
import com.techie.dharmaraj.bakingapp.SimpleIdlingResource;
import com.techie.dharmaraj.bakingapp.adapters.RecipeCardAdapter;
import com.techie.dharmaraj.bakingapp.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,RecipeCardAdapter.CardListItemClickListener{

    @BindView(R.id.main_activity_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.network_error_view) ImageView networkErrorMessageView;

   @Nullable private SimpleIdlingResource mSimpleIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager;

        //if the device is in landscape mode we will display a grid with 2 column
        //by default (i.e)in the portrait mode it will have a single column which will make it a list
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridLayoutManager = new GridLayoutManager(this,2);
        }else {
            gridLayoutManager = new GridLayoutManager(this,1);
        }
        mRecyclerView.setLayoutManager(gridLayoutManager);
        showProgressBar();
        getIdlingResource();
    }

    @Override
    protected void onStart() {
        if(isNetworkAvailable()){
            getSupportLoaderManager().initLoader(0,null,this).forceLoad();
        }else{
            showNetWorkError();
        }
        super.onStart();
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource(){
        if(mSimpleIdlingResource == null){
            mSimpleIdlingResource = new SimpleIdlingResource();
        }
        return mSimpleIdlingResource;
    }

    @Override
    public Loader<String > onCreateLoader(int id, Bundle args) {
        return new RecipeLoader(this,mSimpleIdlingResource);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String  data) {
        if (mSimpleIdlingResource != null) {
            mSimpleIdlingResource.setIdleState(true);
        }
        if(data != null ){
            //we set jsonObject of the utils class with response we got from network
            JsonUtils.setmJsonObjects(JsonUtils.getRecipeJsonObjects(data));

            //set the mRecyclerView with the adapter
            RecipeCardAdapter recipeCardAdapter = new RecipeCardAdapter(JsonUtils.getRecipesCardDetails(),this);
            mRecyclerView.setAdapter(recipeCardAdapter);
            showListOfData();
        } else {
            showNetWorkError();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    /**
     * method to check weather the device is connected to internet or not.
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    /**
     * show only the data and hide the progressbar and error message
     */
    public void showListOfData(){
        progressBar.setVisibility(View.INVISIBLE);
        networkErrorMessageView.setVisibility(View.INVISIBLE);
    }

    /**
     * show only the network error message and hide the rest
     */
    public void showNetWorkError(){
        progressBar.setVisibility(View.INVISIBLE);
        networkErrorMessageView.setVisibility(View.VISIBLE);
    }

    /**
     * show the progressbar to indicate that a background task is running
     */
    public void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        networkErrorMessageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCardListItemClicked(int position) {
        Intent intent = new Intent(this,StepsActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }
}

