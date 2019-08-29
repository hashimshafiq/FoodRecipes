package com.hashimshafiq.foodrecipies.respositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.hashimshafiq.foodrecipies.models.Recipe;
import com.hashimshafiq.foodrecipies.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private RecipeApiClient mRecipeApiClient;
    private static RecipeRepository instance;
    private String mQuery;
    private int mPageNumber;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();

    public static RecipeRepository getInstance(){
        if(instance==null)
            instance = new RecipeRepository();

        return instance;
    }

    private RecipeRepository() {
        mRecipeApiClient = RecipeApiClient.getInstance();
        initMediators();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }
    public LiveData<Recipe> getRecipe(){
        return mRecipeApiClient.getRecipe();
    }

    private void initMediators(){
        LiveData<List<Recipe>> recipeListApiSource = mRecipeApiClient.getRecipes();
        mRecipes.addSource(recipeListApiSource, recipes -> {
            if(recipes != null){
                mRecipes.setValue(recipes);
                doneQuery(recipes);
            }else{
                doneQuery(null);
            }
        });
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mIsQueryExhausted;
    }

    private void doneQuery(List<Recipe> list){
        if(list!=null){
            if(list.size() % 30 != 0){
                mIsQueryExhausted.setValue(true);
            }
        }else{
            mIsQueryExhausted.setValue(true);
        }
    }

    public void searchRecipeApi(String query, int pageNumber){
        if(pageNumber==0){
            pageNumber = 1;
        }
        mQuery = query;
        mPageNumber = pageNumber;
        mIsQueryExhausted.setValue(false);
        mRecipeApiClient.searchRecipeApi(query,pageNumber);
    }

    public void cancelRequest(){
        mRecipeApiClient.cancelRequest();
    }

    public void searchNextPage(){
         searchRecipeApi(mQuery,mPageNumber+1);
    }

    public void searchRecipeById(String recipeId){
        mRecipeApiClient.searchRecipeById(recipeId);
    }

    public LiveData<Boolean> isRecipeRequestTimedOut(){
        return mRecipeApiClient.isRecipeRequestTimedOut();
    }


}
