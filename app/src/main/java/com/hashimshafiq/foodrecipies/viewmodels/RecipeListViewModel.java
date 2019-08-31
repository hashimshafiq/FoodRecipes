package com.hashimshafiq.foodrecipies.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class RecipeListViewModel extends AndroidViewModel {

    public enum ViewState {CATEGORIES,RECIPES};

    public MutableLiveData<ViewState> viewState;

    private static final String TAG = "RecipeListViewModel";

    public RecipeListViewModel(Application application) {
        super(application);

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



}
