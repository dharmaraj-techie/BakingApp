package com.techie.dharmaraj.bakingapp.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techie.dharmaraj.bakingapp.R;
import com.techie.dharmaraj.bakingapp.adapters.StepsAdapter;
import com.techie.dharmaraj.bakingapp.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsActivityFragment extends Fragment implements StepsAdapter.ListItemClickListener {
    //we use this key to send the current recipe index to the fragment from activity
    public static final String RECIPE_INDEX_KEY = "recipeIndex";
    //current recipe index
    private int mRecipeStepsIndex;
    //listener callback reference
    StepsClickCallBack mCallBack;
    private Unbinder unbinder;

    @BindView(R.id.steps_recycler_view) RecyclerView stepsRecyclerView;

    public StepsActivityFragment() {
        // Required empty public constructor
    }

    /**
     * this method is used to construct this Fragment by passing a current recipe index through this
     * method and initializing it in this fragment
     */
    public static StepsActivityFragment newInstance(int recipeIndex) {
        StepsActivityFragment fragment = new StepsActivityFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_INDEX_KEY, recipeIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mRecipeStepsIndex = getArguments().getInt(RECIPE_INDEX_KEY);
        }
    }

    //interface which handles click event callbacks
    public interface StepsClickCallBack{
         void clickCallBack(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBack = (StepsClickCallBack) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_steps_activity, container, false);
        unbinder = ButterKnife.bind(this,view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        stepsRecyclerView.setLayoutManager(linearLayoutManager);
        StepsAdapter adapter = new StepsAdapter(JsonUtils.getRecipeStepsDescription(mRecipeStepsIndex),this);
        stepsRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onListItemClicked(int position) {
        //call the listener with clicked position
        mCallBack.clickCallBack(position);
    }
}
