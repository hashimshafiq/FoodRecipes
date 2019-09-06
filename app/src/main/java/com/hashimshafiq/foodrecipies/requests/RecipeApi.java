package com.hashimshafiq.foodrecipies.requests;

import androidx.lifecycle.LiveData;

import com.hashimshafiq.foodrecipies.requests.responses.RecipeResponse;
import com.hashimshafiq.foodrecipies.requests.responses.RecipesSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    //Search Recipe API
    @GET("api/search")
    LiveData<ApiResponse<RecipesSearchResponse>> searchRecipe(
            @Query("key") String key,
            @Query("q") String query,
            @Query("page") String page
    );

    // GET Recipe Request

    @GET("api/get")
    LiveData<ApiResponse<RecipeResponse>> getRecipe(
            @Query("key") String key,
            @Query("rId") String recipe_id
    );
}
