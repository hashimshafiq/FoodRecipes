package com.hashimshafiq.foodrecipies.requests;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hashimshafiq.foodrecipies.AppExecutors;
import com.hashimshafiq.foodrecipies.models.Recipe;
import com.hashimshafiq.foodrecipies.requests.responses.RecipesSearchResponse;
import com.hashimshafiq.foodrecipies.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.hashimshafiq.foodrecipies.utils.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private MutableLiveData<List<Recipe>> mRecipes;
    private static RecipeApiClient instance;
    private RetrieveRecipesRunnable mRetrieveRecipesRunnable;

    public static RecipeApiClient getInstance(){
        if(instance==null)
            instance = new RecipeApiClient();

        return instance;
    }

    private RecipeApiClient(){
        mRecipes = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipe(){
        return mRecipes;
    }

    public void searchRecipeApi(String query, int pageNumber){
        if(mRetrieveRecipesRunnable != null){
            mRetrieveRecipesRunnable = null;
        }
        mRetrieveRecipesRunnable = new RetrieveRecipesRunnable(query,pageNumber);

        final Future handler = AppExecutors.getInstance().getNetworkIO().submit(mRetrieveRecipesRunnable);

        AppExecutors.getInstance().getNetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);

            }
        },NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveRecipesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        private boolean cancelRequest;

        public RetrieveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            this.cancelRequest = false;
        }



        @Override
        public void run() {
            try {
                Response response = getRecipes(query,pageNumber).execute();
                if(cancelRequest) {
                    return;
                }

                if(response.code()==200){
                    List<Recipe> list = new ArrayList<>(((RecipesSearchResponse)response.body()).getRecipes());
                    if(pageNumber==1){
                        mRecipes.postValue(list);
                    }else{
                        List<Recipe> currentList = mRecipes.getValue();
                        currentList.addAll(list);
                        mRecipes.postValue(currentList);
                    }

                }else{
                    mRecipes.postValue(null);
                }

            } catch (IOException e) {
                mRecipes.postValue(null);
                e.printStackTrace();
            }



        }

        private Call<RecipesSearchResponse> getRecipes(String query, int pageNumber){
            return ServiceGenerator.getRecipeApi().searchRecipe(Constants.API_KEY,query,String.valueOf(pageNumber));
        }

        private void cancelRequest(){

        }
    }
}
