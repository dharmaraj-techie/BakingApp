package com.techie.dharmaraj.bakingapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.techie.dharmaraj.bakingapp.R;

public class ViewStepsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_steps);

        //get the recipe index and position (i.e) which step has been clicked from the intents
        int stepAtPosition = getIntent().getIntExtra("position", 0);
        int recipeIndex = getIntent().getIntExtra("mRecipeIndex", 0);

        if(savedInstanceState == null){
        ViewStepsActivityFragment stepsActivityFragment = ViewStepsActivityFragment.newInstance(recipeIndex,stepAtPosition);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.view_steps_container,stepsActivityFragment)
                .commit();
        }
    }
}
