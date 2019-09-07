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
import com.hashimshafiq.foodrecipies.utils.Resource;
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
            subscribeObserver(recipe.getRecipe_id());

        }
    }

    private void subscribeObserver(String recipeId){
        mRecipeViewModel.searchRecipesApi(recipeId).observe(this, new Observer<Resource<Recipe>>() {
            @Override
            public void onChanged(Resource<Recipe> recipeResource) {
                if(recipeResource != null){
                    if(recipeResource.data != null){
                        switch (recipeResource.status){

                            case LOADING:{
                                showProgressBar(true);
                                break;
                            }

                            case ERROR:{
                                showParent();
                                showProgressBar(false);
                                setRecipeProperties(recipeResource.data);
                                break;
                            }

                            case SUCCESS:{
                                showParent();
                                showProgressBar(false);
                                setRecipeProperties(recipeResource.data);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }


    private void setRecipeProperties(Recipe recipe){
        if(recipe != null){
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background);

            Glide.with(this)
                    .setDefaultRequestOptions(options)
                    .load(recipe.getImage_url())
                    .into(mRecipeImage);

            mRecipeTitle.setText(recipe.getTitle());
            mRecipeRank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

            setIngredients(recipe);
        }
    }

    private void setIngredients(Recipe recipe){
        mRecipeIngredientsContainer.removeAllViews();

        if(recipe.getIngredients() != null){
            for(String ingredient: recipe.getIngredients()){
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setTextSize(15);
                textView.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                mRecipeIngredientsContainer.addView(textView);
            }
        }
        else{
            TextView textView = new TextView(this);
            textView.setText("Error retrieving ingredients.\nCheck network connection.");
            textView.setTextSize(15);
            textView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            mRecipeIngredientsContainer.addView(textView);
        }
    }



    private void showParent() {
        mScrollView.setVisibility(View.VISIBLE);
    }



}

