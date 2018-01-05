package com.techie.dharmaraj.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techie.dharmaraj.bakingapp.R;
import com.techie.dharmaraj.bakingapp.data.RecipeCard;
import com.techie.dharmaraj.bakingapp.utils.JsonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dharmaraj on 05-12-2017.
 */

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.RecipeCardViewHolder> {
    private ArrayList<RecipeCard> recipeCards;
    private CardListItemClickListener mCardListItemClickListener;
    public RecipeCardAdapter(ArrayList<RecipeCard> recipeCards,CardListItemClickListener listener){
        mCardListItemClickListener = listener;
        this.recipeCards = recipeCards;
    }
    @Override
    public RecipeCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item, parent, false);
        return new RecipeCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeCardViewHolder holder, int position) {
        String name = recipeCards.get(position).getRecipeName();
        String url = recipeCards.get(position).getRecipeImageUrl();
        holder.bind(name,url);
    }

    @Override
    public int getItemCount() {
        if(recipeCards != null){
            return recipeCards.size();
        }else {
            return 0;
        }
    }

    class RecipeCardViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recipe_name_tv)TextView recipeNameTextView;
        @BindView(R.id.image_view)
        ImageView recipeImageView;
        public RecipeCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCardListItemClickListener.onCardListItemClicked(getAdapterPosition());
                }
            });
        }

        public void bind(String name,String url){
            recipeNameTextView.setText(name);
            if(url.isEmpty() || url == null){
                recipeImageView.setImageResource(
                        JsonUtils.getRecipeImageResourceId(getAdapterPosition()));
            }else {
                Glide.with(itemView.getContext()).load(url).into(recipeImageView);
            }
        }

    }
    /**
     * interface for item click listener
     */
    public interface CardListItemClickListener {
        void onCardListItemClicked(int position);
    }
}
