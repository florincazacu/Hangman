package com.hangman.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Florin on 23-05-2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CategoryHolder> {

    private ArrayList<String> mCategories;

    public RecyclerAdapter(ArrayList<String> categories) {
        mCategories = categories;
    }

    //1
    public static class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //2
        private TextView categoryName;
        private String mCategory;

        //3
        private static final String CATEGORY_KEY = "CATEGORY";

        //4
        public CategoryHolder(View v) {
            super(v);

            categoryName = (TextView) v.findViewById(R.id.category_name);
            v.setOnClickListener(this);
        }

        //5
        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();
            Intent startGameActivity = new Intent(context, GameActivity.class);
            startGameActivity.putExtra(CATEGORY_KEY, mCategory);
            context.startActivity(startGameActivity);
        }

        public void bindCategory(String category) {
            mCategory = category;
            categoryName.setText(category);
        }
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_items, parent, false);
        return new CategoryHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        for (int i = 0; i < mCategories.size(); i++) {
            String currentCategory = mCategories.get(position);
            holder.bindCategory(currentCategory);
        }
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }
}
