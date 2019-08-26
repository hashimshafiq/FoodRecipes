package com.hashimshafiq.foodrecipies.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hashimshafiq.foodrecipies.models.Recipe;
import com.hashimshafiq.foodrecipies.respositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private boolean mIsViewingRecipes;

    public RecipeListViewModel() {
        mIsViewingRecipes = false;
        mRecipeRepository = RecipeRepository.getInstance();

    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipe();
    }

    public void searchRecipeApi(String query, int pageNumber){
        mIsViewingRecipes = true;
        mRecipeRepository.searchRecipeApi(query,pageNumber);
    }


    public boolean IsViewingRecipes() {
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(boolean mIsViewingRecipes) {
        this.mIsViewingRecipes = mIsViewingRecipes;
    }
}
