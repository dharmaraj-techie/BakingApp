package com.techie.dharmaraj.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techie.dharmaraj.bakingapp.data.Ingredients;
import com.techie.dharmaraj.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * adapter for displaying the list of required ingredients for the recipe
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {
    //stores the arrayList of Ingredients
    private ArrayList<Ingredients> mIngredientsArrayList;

    public IngredientsAdapter(ArrayList<Ingredients> ingredients) {
        mIngredientsArrayList = ingredients;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new IngredientsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        //get the current ingredient from the arrayList of ingredients
        Ingredients ingredient = mIngredientsArrayList.get(position);
        holder.bind(ingredient.getStringIngredient());
    }

    @Override
    public int getItemCount() {
        return mIngredientsArrayList.size();
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_item_text_view)
        TextView ingredientTextView;
        IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * method to bind the string to the textView
         * @param s
         */
        void bind(String s) {
            ingredientTextView.setText(s);
        }
    }
}
