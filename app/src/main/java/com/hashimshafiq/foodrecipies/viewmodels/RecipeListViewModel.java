package com.hashimshafiq.foodrecipies.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.hashimshafiq.foodrecipies.models.Recipe;
import com.hashimshafiq.foodrecipies.respositories.RecipeRepository;
import com.hashimshafiq.foodrecipies.utils.Resource;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    public enum ViewState {CATEGORIES,RECIPES};

    private MutableLiveData<ViewState> viewState;
    private MediatorLiveData<Resource<List<Recipe>>> recipes = new MediatorLiveData<>();
    private RecipeRepository recipeRepository;

    private static final String TAG = "RecipeListViewModel";

    public RecipeListViewModel(Application application) {
        super(application);
        recipeRepository = RecipeRepository.getInstance(application);
        init();
    }

    public void init(){
        if(viewState == null){
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.CATEGORIES);
        }
    }

    public LiveData<ViewState> getViewState(){
        return viewState;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes() {
        return recipes;
    }

    public void searchRecipeApi(String qury, int pageNumber){
        Log.d(TAG, "searchRecipeApi: "+qury);
        final LiveData<Resource<List<Recipe>>> repositorySource = recipeRepository.searchRecipeApi(qury,pageNumber);
        recipes.addSource(repositorySource, listResource -> recipes.setValue(listResource));
    }



}
