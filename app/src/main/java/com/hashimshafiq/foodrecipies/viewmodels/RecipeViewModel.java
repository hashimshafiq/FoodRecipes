package com.hashimshafiq.foodrecipies.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hashimshafiq.foodrecipies.models.Recipe;
import com.hashimshafiq.foodrecipies.respositories.RecipeRepository;
import com.hashimshafiq.foodrecipies.utils.Resource;

public class RecipeViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;
    public RecipeViewModel(Application application){

        super(application);
        recipeRepository = RecipeRepository.getInstance(application);
    }


    public LiveData<Resource<Recipe>> searchRecipesApi(String recipeId){
        return recipeRepository.searchRecipesApi(recipeId);
    }
}
