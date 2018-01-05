package com.techie.dharmaraj.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techie.dharmaraj.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * adapter for displaying the available steps
 */
public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    //Listener which is called when the listItem is clicked
    private ListItemClickListener mClickListener;
    private ArrayList<String> mSteps;

    public StepsAdapter(ArrayList<String> strings, ListItemClickListener clickListener) {
        mSteps = strings;
        //add the first element of the list as Ingredients
        //because it will only contain the available steps. we also want to show the Ingredients
        //so we add it manually
        mSteps.add(0, "Ingredients");
        mClickListener = clickListener;
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new StepsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        holder.bind(mSteps.get(position));
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }


    class StepsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_item_text_view)
        TextView shortDescription;

        StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onListItemClicked(getAdapterPosition());
                }
            });
        }

        /**
         * method to bind the data to the view
         * @param s
         */
        void bind(String s) {
            shortDescription.setText(s);
        }
    }

    /**
     * interface for item click listener
     */
    public interface ListItemClickListener {
        void onListItemClicked(int position);
    }
}
