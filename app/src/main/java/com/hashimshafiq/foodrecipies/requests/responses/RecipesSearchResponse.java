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

    @SerializedName("error")
    @Expose
    private String error;

    public int getCount() {
        return count;
    }

    public String getError() {
        return error;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    @Override
    public String toString() {
        return "RecipesSearchResponse{" +
                "count=" + count +
                ", recipes=" + recipes +
                ", error='" + error + '\'' +
                '}';
    }
}
