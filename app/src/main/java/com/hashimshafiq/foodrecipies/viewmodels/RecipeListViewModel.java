package com.hashimshafiq.foodrecipies.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.hashimshafiq.foodrecipies.models.Recipe;
import com.hashimshafiq.foodrecipies.respositories.RecipeRepository;
import com.hashimshafiq.foodrecipies.utils.Resource;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    public enum ViewState {CATEGORIES,RECIPES}

    private MutableLiveData<ViewState> viewState;
    private MediatorLiveData<Resource<List<Recipe>>> recipes = new MediatorLiveData<>();
    private RecipeRepository recipeRepository;

    // extra query var
    private int pageNumber;
    private String query;
    private boolean isPerformingQuery;
    private boolean isQueryEXhausted;
    private boolean cancelRequest;


    private static final String TAG = "RecipeListViewModel";
    public static final String QUERY_EXHAUSTED = "No more results..";

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

    public void searchRecipeApi(String query, int pageNumber){
        if(pageNumber == 0){
            pageNumber = 1;
        }
        this.pageNumber = pageNumber;
        this.query = query;
        executeQuery();

    }

    public void setCategoryView(){
        viewState.setValue(ViewState.CATEGORIES);
    }

    private void executeQuery(){
        cancelRequest = false;
        this.isPerformingQuery = true;
        viewState.setValue(ViewState.RECIPES);
        final LiveData<Resource<List<Recipe>>> repositorySource = recipeRepository.searchRecipeApi(query,pageNumber);
        recipes.addSource(repositorySource, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> listResource) {
                if(!cancelRequest){
                    if(listResource != null){
                        recipes.setValue(listResource);
                        if(listResource.status == Resource.Status.SUCCESS){
                            isPerformingQuery = false;
                            if(listResource.data != null){
                                if(listResource.data.size()==0){
                                    recipes.setValue(new Resource<List<Recipe>>(
                                            Resource.Status.ERROR,
                                            listResource.data,
                                            QUERY_EXHAUSTED

                                    ));
                                }
                            }
                            recipes.removeSource(repositorySource);
                        }else if(listResource.status == Resource.Status.ERROR){
                            isPerformingQuery = false;
                            recipes.removeSource(repositorySource);
                        }

                    }else{
                        recipes.removeSource(repositorySource);
                    }
                }else{
                    recipes.removeSource(repositorySource);
                }

            }
        });
    }


    public int getPageNumber() {
        return pageNumber;
    }

    public void searchNextPage(){
        if(!isQueryEXhausted && !isPerformingQuery){
            pageNumber++;
            executeQuery();
        }
    }

    public void cancelSearchRequest(){
        if(isPerformingQuery){
            cancelRequest = true;
            isPerformingQuery = false;
            pageNumber = 1;
        }
    }
}
