package com.hangman.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Florin on 23-05-2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CategoryHolder> {

    public static final String TAG = "RecyclerAdapter";

    private HashMap<String, String> mCategories;

    public RecyclerAdapter(HashMap<String, String> categories) {
        Log.d(TAG, "Map " + categories);
        mCategories = categories;
    }

    //1
    public static class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //2
        private Button categoryButton;
        private String mCategory;

        //3
        private static final String CATEGORY_KEY = "CATEGORY";
        private static final String GS_KEY = "GS";

        //4
        public CategoryHolder(View v) {
            super(v);
            categoryButton = (Button) v.findViewById(R.id.category_button);
            categoryButton.setOnClickListener(this);
        }

        //5
        @Override
        public void onClick(View v) {
            Button b = (Button)v;
            Log.d(TAG, "gs reference" + v.getTag().toString());
            Context context = itemView.getContext();
            Intent startGameActivity = new Intent(context, GameActivity.class);
            startGameActivity.putExtra(CATEGORY_KEY, b.getText().toString());
            startGameActivity.putExtra(GS_KEY, v.getTag().toString());
            context.startActivity(startGameActivity);
        }

        public void bindCategory(String category, String gsReference) {
            Log.d(TAG, "bindCategory " + category + " " + gsReference);
            mCategory = category;
            categoryButton.setText(category);
            categoryButton.setTag(gsReference);
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
        for (Map.Entry<String, String> entry : mCategories.entrySet()) {
            String currentCategory = mCategories.get(entry.getKey());
            Log.d(TAG, "getKey " + currentCategory);
            String currentGsReference = mCategories.get(entry.getValue());
            Log.d(TAG, "getValue " + currentGsReference);
            holder.bindCategory(currentCategory, currentGsReference);
        }
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }
}
