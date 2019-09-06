package com.hashimshafiq.foodrecipies.respositories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.hashimshafiq.foodrecipies.AppExecutors;
import com.hashimshafiq.foodrecipies.models.Recipe;
import com.hashimshafiq.foodrecipies.persistence.RecipeDao;
import com.hashimshafiq.foodrecipies.persistence.RecipesDatabase;
import com.hashimshafiq.foodrecipies.requests.ApiResponse;
import com.hashimshafiq.foodrecipies.requests.ServiceGenerator;
import com.hashimshafiq.foodrecipies.requests.responses.RecipesSearchResponse;
import com.hashimshafiq.foodrecipies.utils.Constants;
import com.hashimshafiq.foodrecipies.utils.NetworkBoundResource;
import com.hashimshafiq.foodrecipies.utils.Resource;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeDao recipeDao;

    private RecipeRepository(Context context) {
        recipeDao = RecipesDatabase.getInstance(context).getRecipeDao();
    }

    public static RecipeRepository getInstance(Context context) {
        if(instance == null){
            instance = new RecipeRepository(context);
        }

        return instance;
    }

    public LiveData<Resource<List<Recipe>>> searchRecipeApi(String query, int pageNumber){
        return new NetworkBoundResource<List<Recipe>, RecipesSearchResponse>(AppExecutors.getInstance()){
            @Override
            protected void saveCallResult(@NonNull RecipesSearchResponse item) {
                if(item.getRecipes() != null){

                    Recipe[] recipes = new Recipe[item.getRecipes().size()];
                    int index = 0;
                    for( long rowid : recipeDao.insertRecipes(item.getRecipes().toArray(recipes))){
                        if(rowid == -1){
                            recipeDao.updateRecipe(recipes[index].getRecipe_id(),recipes[index].getTitle(),recipes[index].getPublisher(),recipes[index].getImage_url(),recipes[index].getSocial_rank());
                        }
                        index++;
                    }




                }

            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                return true;
            }

            @Override
            protected LiveData<List<Recipe>> loadFromDb() {

                return recipeDao.searchRecipe(query,pageNumber);
            }

            @Override
            protected LiveData<ApiResponse<RecipesSearchResponse>> createCall() {
                return ServiceGenerator.getRecipeApi().searchRecipe(Constants.API_KEY,query,String.valueOf(pageNumber));
            }
        }.getAsLiveData();

    }
}
