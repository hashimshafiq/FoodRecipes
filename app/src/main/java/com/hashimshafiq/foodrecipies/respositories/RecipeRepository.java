package com.hashimshafiq.foodrecipies.respositories;

import androidx.lifecycle.LiveData;

import com.hashimshafiq.foodrecipies.models.Recipe;
import com.hashimshafiq.foodrecipies.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private RecipeApiClient mRecipeApiClient;
    private static RecipeRepository instance;

    public static RecipeRepository getInstance(){
        if(instance==null)
            instance = new RecipeRepository();

        return instance;
    }

    private RecipeRepository() {
        mRecipeApiClient = RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipe(){
        return mRecipeApiClient.getRecipe();
    }

    public void searchRecipeApi(String query, int pageNumber){
        if(pageNumber==0){
            pageNumber = 1;
        }
        mRecipeApiClient.searchRecipeApi(query,pageNumber);
    }


}
