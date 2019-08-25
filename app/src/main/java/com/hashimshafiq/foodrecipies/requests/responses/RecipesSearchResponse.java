package com.hashimshafiq.foodrecipies.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hashimshafiq.foodrecipies.models.Recipe;

import java.util.List;

public class RecipesSearchResponse {

    @SerializedName("count")
    @Expose()
    private int count;

    @SerializedName("recipes")
    @Expose
    private List<Recipe> recipes;

    public int getCount() {
        return count;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    @Override
    public String toString() {
        return "RecipesSearchResponse{" +
                "count=" + count +
                ", recipes=" + recipes +
                '}';
    }
}
