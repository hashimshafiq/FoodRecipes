package com.hashimshafiq.foodrecipies.requests.responses;

public class CheckApiRecipeKey {

    public static boolean isRecipeApiKeyValid(RecipeResponse recipeResponse){
        return recipeResponse.getError() == null;
    }


    public static boolean isRecipeApiKeyValid(RecipesSearchResponse recipeResponse){
        return recipeResponse.getError() == null;
    }
}
