package com.hangman.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Florin on 23-05-2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CategoryHolder> {

    public static final String TAG = "RecyclerAdapter";

    private ArrayList<Category> mCategories;

    public RecyclerAdapter(ArrayList<Category> categories) {
        mCategories = categories;
    }

    //1
    public static class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //2
        private Button categoryButton;
        private Category mCategory;

        //3
        private static final String CATEGORY_KEY = "CATEGORY";
        private static final String GS_KEY = "GS_KEY";

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
            Log.d(TAG, "gs reference " + v.getTag().toString());
            Context context = itemView.getContext();
            Intent startGameActivity = new Intent(context, GameActivity.class);
            startGameActivity.putExtra(CATEGORY_KEY, b.getText().toString().toLowerCase());
            startGameActivity.putExtra(GS_KEY, v.getTag().toString());
            Log.d(TAG, "b.getText " + b.getText() + " b.getTag " + v.getTag());
            context.startActivity(startGameActivity);
        }

        public void bindCategory(Category category) {
//            Log.d(TAG, "bindCategory " + selectedCategory + " " + gsReference);
            mCategory = category;
            categoryButton.setText(mCategory.getName());
            categoryButton.setTag(mCategory.getGs_reference());
        }
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_items, parent, false);
        return new CategoryHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.CategoryHolder holder, int position) {

        Category itemCategory = mCategories.get(position);
        holder.bindCategory(itemCategory);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }
}
