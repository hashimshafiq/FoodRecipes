package com.hashimshafiq.foodrecipies.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.hashimshafiq.foodrecipies.R;
import com.hashimshafiq.foodrecipies.listeners.OnRecipeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RecipeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.recipe_title)
    public TextView mRecipeTitle;
    @BindView(R.id.recipe_publisher)
    public TextView mRecipePublisher;
    @BindView(R.id.recipe_image)
    public AppCompatImageView mRecipeImageView;
    @BindView(R.id.recipe_social_score)
    public TextView mRecipeSocialScore;

    OnRecipeListener onRecipeListener;

    public RecipeViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.onRecipeListener = onRecipeListener;
    }

    @OnClick(R.id.root) void onClickRecipe(){
        onRecipeListener.onRecipeClick(getAdapterPosition());
    }


}
