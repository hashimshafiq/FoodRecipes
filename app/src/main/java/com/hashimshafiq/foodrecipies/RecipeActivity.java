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
        showProgressBar(true);
        subscribeObservers();
        getIncomingIntent();

    }



    private void getIncomingIntent(){
        if(getIntent().hasExtra("recipe")){
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            mRecipeViewModel.SearchRecipeById(recipe.getRecipe_id());
        }
    }

    private void subscribeObservers(){
        mRecipeViewModel.getRecipe().observe(this, recipe -> {
            if(recipe != null){
                if(recipe.getRecipe_id().equals(mRecipeViewModel.getRecipeId())){
                    setRecipeProperties(recipe);
                    mRecipeViewModel.setRetrievedRecipe(true);
                }

            }
        });

        mRecipeViewModel.isRecipeRequestTimedOut().observe(this, aBoolean -> {
            if(aBoolean && !mRecipeViewModel.isRetrievedRecipe()){
                Log.d(TAG, "onChanged: timed out");
                displayErrorScreen("Error retrieving data. Check network connection");
            }
        });
    }

    private void setRecipeProperties(Recipe recipe){
        if(recipe != null){
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipe.getImage_url())
                    .into(mRecipeImage);

            mRecipeTitle.setText(recipe.getTitle());
            mRecipeRank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

            mRecipeIngredientsContainer.removeAllViews();
            for(String ingredient: recipe.getIngredients()){
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                mRecipeIngredientsContainer.addView(textView);
            }
        }

        showParent();
        showProgressBar(false);

    }

    private void showParent() {
        mScrollView.setVisibility(View.VISIBLE);
    }


    private void displayErrorScreen(String errorMessage){
        mRecipeTitle.setText("Error retrieving recipe...");
        mRecipeRank.setText("");
        TextView textView = new TextView(this);
        if(!errorMessage.equals("")){
            textView.setText(errorMessage);
        }
        else{
            textView.setText("Error");
        }
        textView.setTextSize(15);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        mRecipeIngredientsContainer.addView(textView);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(R.drawable.ic_launcher_background)
                .into(mRecipeImage);

        showParent();
        showProgressBar(false);
    }
}

