package com.techie.dharmaraj.bakingapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.techie.dharmaraj.bakingapp.R;

public class IngredientsActivity extends AppCompatActivity {
    //variable to store current recipe Index
    private static int ingredientRecipeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        ingredientRecipeIndex = getIntent().getIntExtra("mRecipeIndex", 0);
        //we don't want to add the fragment again when the device is rotated so
        //we check that we have previous state if so means we don't create the new fragment and add it
        if(savedInstanceState == null) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            IngredientsActivityFragment ingredientFragment = IngredientsActivityFragment.newInstance(ingredientRecipeIndex);
            fragmentManager.beginTransaction()
                    .add(R.id.ingredient_container, ingredientFragment)
                    .commit();
        }
    }

    //returns the current recipe we are looking at
    public static int getIngredientRecipeIndex(){
        return ingredientRecipeIndex;
    }
}
