package com.hashimshafiq.foodrecipies.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hashimshafiq.foodrecipies.models.Recipe;
import com.hashimshafiq.foodrecipies.respositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();

    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipe();
    }

    public void searchRecipeApi(String query, int pageNumber){
        mRecipeRepository.searchRecipeApi(query,pageNumber);
    }
}
