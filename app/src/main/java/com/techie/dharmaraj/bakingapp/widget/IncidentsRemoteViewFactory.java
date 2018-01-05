package com.techie.dharmaraj.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.techie.dharmaraj.bakingapp.R;
import com.techie.dharmaraj.bakingapp.data.Ingredients;
import com.techie.dharmaraj.bakingapp.ui.IngredientsActivity;
import com.techie.dharmaraj.bakingapp.utils.JsonUtils;

import java.util.ArrayList;

/**
 * Created by Dharmaraj on 24-11-2017.
 */

public class IncidentsRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    //variable to store the context of the app
    private Context mContext;
    //list of ingredients
    private ArrayList<Ingredients> mIngredientsArrayList;

    public IncidentsRemoteViewFactory(Context appContext) {
        mContext = appContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        if(JsonUtils.mJsonObjects!=null){
            //when the data set is changed we fetch the new value
            mIngredientsArrayList = JsonUtils.getRecipeIngredents(IngredientsActivity.getIngredientRecipeIndex());
        }
    }

    @Override
    public void onDestroy() {
        mIngredientsArrayList = null;
    }

    @Override
    public int getCount() {
        if (mIngredientsArrayList == null) return 0;
        return mIngredientsArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        //get the readable ingredient
        String ingredient = mIngredientsArrayList.get(i).getStringIngredient();
        //set it to the widget's list of textViews
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        views.setTextViewText(R.id.widget_list_item_text_view, ingredient);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Created by Dharmaraj on 24-11-2017.
     */
    public static class IngredientRemoteViewService extends RemoteViewsService {
        @Override
        public RemoteViewsFactory onGetViewFactory(Intent intent) {
            return new IncidentsRemoteViewFactory(this.getApplicationContext());
        }
    }
}
