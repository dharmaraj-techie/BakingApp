package com.techie.dharmaraj.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.techie.dharmaraj.bakingapp.R;

public class StepsActivity extends AppCompatActivity implements StepsActivityFragment.StepsClickCallBack {

    //boolean value to know whether the device is tablet or mobile
    boolean mIsTwoPane;
    //variable for the current recipe's index
    private int recipeStepsIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        //getting the current recipe's index from the intent
        recipeStepsIndex = getIntent().getIntExtra("position", 0);

        //we don't want to create another fragment when the device is rotated
        if (savedInstanceState == null) {
            StepsActivityFragment stepsFragment = StepsActivityFragment.newInstance(recipeStepsIndex);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.steps_container, stepsFragment)
                    .commit();
        }

        //we check if the layout contains the id of the LinearLayout which will only present in the twoPane Layout
        //if it is present means the device is a tablet, so make the boolean variable true
        if (findViewById(R.id.two_pane_layout) != null) {
            IngredientsActivityFragment fragment = new IngredientsActivityFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.steps_detail_view_container, fragment)
                    .commit();
            mIsTwoPane = true;
        } else {
            mIsTwoPane = false;
        }
    }

    /**
     * this method will be called when the list item is clicked
     * @param position
     */
    @Override
    public void clickCallBack(int position) {

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        //we check if it is clicked from a twoPane mode or normal mode
        if(mIsTwoPane){
            //if it is clicked from twoPane mode means we don't pass any intent we simple replace the
            //previous fragment with the new one. because they both will present the same activity
            if(position == 0) {
                IngredientsActivityFragment fragment = new IngredientsActivityFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.steps_detail_view_container,fragment)
                        .commit();
            } else{
                ViewStepsActivityFragment fragment = ViewStepsActivityFragment.newInstance(recipeStepsIndex,--position);
                fragmentManager.beginTransaction()
                        .replace(R.id.steps_detail_view_container,fragment)
                        .commit();
            }
        //if not means we pass an intent to launch activity
        }else{
            if(position == 0) {
                Intent intent = new Intent(this,IngredientsActivity.class);
                intent.putExtra("mRecipeIndex", recipeStepsIndex);
                startActivity(intent);
            } else{
                Intent intent = new Intent(this,ViewStepsActivity.class);
                intent.putExtra("mRecipeIndex", recipeStepsIndex);
                intent.putExtra("position", --position);
                startActivity(intent);
            }
        }
    }
}
