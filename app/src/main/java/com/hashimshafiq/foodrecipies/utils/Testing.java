package com.hashimshafiq.foodrecipies.utils;

import android.util.Log;


import com.hashimshafiq.foodrecipies.models.Recipe;

import java.util.List;

public class Testing {

    public static void printRecipes(List<Recipe>list, String tag){
        for(Recipe recipe: list){
            Log.d(tag, "onChanged: " + recipe.getTitle());
        }
    }
}
