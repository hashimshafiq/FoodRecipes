package com.hashimshafiq.foodrecipies;

import android.os.Bundle;
import android.view.View;

import com.hashimshafiq.foodrecipies.requests.RecipeApi;
import com.hashimshafiq.foodrecipies.requests.ServiceGenerator;
import com.hashimshafiq.foodrecipies.requests.responses.RecipesSearchResponse;
import com.hashimshafiq.foodrecipies.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        findViewById(R.id.but).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               testRequest();
            }
        });

    }

    private void testRequest(){
        RecipeApi api = ServiceGenerator.getRecipeApi();
        Call<RecipesSearchResponse> call = api.searchRecipe(Constants.API_KEY,"chicken","1");
        call.enqueue(new Callback<RecipesSearchResponse>() {
            @Override
            public void onResponse(Call<RecipesSearchResponse> call, Response<RecipesSearchResponse> response) {
                if(response.isSuccessful()){
                    System.out.println(response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<RecipesSearchResponse> call, Throwable t) {

            }
        });
    }
}
