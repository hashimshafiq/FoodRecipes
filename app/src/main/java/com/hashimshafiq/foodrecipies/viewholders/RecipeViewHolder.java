package com.hashimshafiq.foodrecipies.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.hashimshafiq.foodrecipies.R;
import com.hashimshafiq.foodrecipies.listeners.OnRecipeListener;
import com.hashimshafiq.foodrecipies.models.Recipe;

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
    RequestManager requestManager;
    ViewPreloadSizeProvider viewPreloadSizeProvider;

    public RecipeViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener, RequestManager requestManager,ViewPreloadSizeProvider viewPreloadSizeProvider) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.onRecipeListener = onRecipeListener;
        this.requestManager = requestManager;
        this.viewPreloadSizeProvider = viewPreloadSizeProvider;
    }

    @OnClick(R.id.root) void onClickRecipe(){
        onRecipeListener.onRecipeClick(getAdapterPosition());
    }

    public void onBind(Recipe recipe){


        requestManager
                .load(recipe.getImage_url())
                .into(mRecipeImageView);

        mRecipeTitle.setText(recipe.getTitle());
        mRecipePublisher.setText(recipe.getPublisher());
        mRecipeSocialScore.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

        viewPreloadSizeProvider.setView(mRecipeImageView);
    }


}
