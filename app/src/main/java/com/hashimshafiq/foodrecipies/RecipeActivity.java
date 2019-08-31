package com.hashimshafiq.foodrecipies;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hashimshafiq.foodrecipies.models.Recipe;
import com.hashimshafiq.foodrecipies.viewmodels.RecipeViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity  extends BaseActivity{

    @BindView(R.id.recipe_image)
    AppCompatImageView mRecipeImage;
    @BindView(R.id.recipe_title)
    TextView mRecipeTitle;
    @BindView(R.id.recipe_social_score)
    TextView mRecipeRank;
    @BindView(R.id.ingredients_container)
    LinearLayout mRecipeIngredientsContainer;
    @BindView(R.id.parent)
    ScrollView mScrollView;

    RecipeViewModel mRecipeViewModel;

    private static final String TAG = "RecipeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        getIncomingIntent();

    }



    private void getIncomingIntent(){
        if(getIntent().hasExtra("recipe")){
            Recipe recipe = getIntent().getParcelableExtra("recipe");

        }
    }



    private void showParent() {
        mScrollView.setVisibility(View.VISIBLE);
    }



}

