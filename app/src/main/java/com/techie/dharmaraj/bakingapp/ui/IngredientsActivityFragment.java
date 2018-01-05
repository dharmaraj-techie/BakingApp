package com.techie.dharmaraj.bakingapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.techie.dharmaraj.bakingapp.R;
import com.techie.dharmaraj.bakingapp.adapters.IngredientsAdapter;
import com.techie.dharmaraj.bakingapp.utils.JsonUtils;
import com.techie.dharmaraj.bakingapp.widget.UpdateWidgetIntentService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsActivityFragment extends Fragment {
    //we use this key to send the current recipe index to the fragment from activity
    public static final String RECIPE_INDEX_KEY = "recipeIndex";
    //current recipe index
    private int mRecipeIndex;
    @BindView(R.id.added_to_widget_tv) TextView addedToWidgetTextView;
    @BindView(R.id.steps_btn)Button stepsButton;
    @BindView(R.id.add_to_widget_btn)Button addToWidgetButton;
    @BindView(R.id.ingredient_recycler_view)RecyclerView ingredientRecyclerView;

    //boolean variable to check the device is tablet or mobile
    boolean isTwoPaneMode;
    private Unbinder unbinder;
    public IngredientsActivityFragment() {
        // Required empty public constructor
    }
    /**
     * this method is used to construct this Fragment by passing a current recipe index through this
     * method and initializing it in this fragment
     */
    public static IngredientsActivityFragment newInstance(int recipeIndex) {
        IngredientsActivityFragment fragment = new IngredientsActivityFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_INDEX_KEY, recipeIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            //get the recipe index from the arguments passed to this fragment
            mRecipeIndex = getArguments().getInt(RECIPE_INDEX_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ingredients,container,false);
        //get boolean value.
        isTwoPaneMode = getActivity().getResources().getBoolean(R.bool.isTwoPaneMode);
        //bind the views to the corresponding ids
        unbinder = ButterKnife.bind(this,view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        ingredientRecyclerView.setLayoutManager(linearLayoutManager);
        ingredientRecyclerView.setHasFixedSize(true);

        IngredientsAdapter adapter = new IngredientsAdapter(JsonUtils.getRecipeIngredents(mRecipeIndex));
        ingredientRecyclerView.setAdapter(adapter);

        //if the device is tablet which has smallest width 600 then we will hide the steps button
        if(!isTwoPaneMode){
            stepsButton.setVisibility(View.VISIBLE);
            stepsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),ViewStepsActivity.class);
                    intent.putExtra("mRecipeIndex", mRecipeIndex);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                }
            });
        }else{
            stepsButton.setVisibility(View.INVISIBLE);
        }
        //when the AddToWidget button is clicked the list of current ingredients is sent to the widget
        addToWidgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateWidgetIntentService.startActionUpdateWidget(view.getContext());
                view.setVisibility(View.INVISIBLE);
                addedToWidgetTextView.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
