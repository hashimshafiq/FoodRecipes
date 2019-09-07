package com.hashimshafiq.foodrecipies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.hashimshafiq.foodrecipies.adapters.RecipeRecyclerAdapter;
import com.hashimshafiq.foodrecipies.listeners.OnRecipeListener;
import com.hashimshafiq.foodrecipies.models.Recipe;
import com.hashimshafiq.foodrecipies.utils.Resource;
import com.hashimshafiq.foodrecipies.utils.Testing;
import com.hashimshafiq.foodrecipies.utils.VerticalSpacingItemDecorator;
import com.hashimshafiq.foodrecipies.viewmodels.RecipeListViewModel;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hashimshafiq.foodrecipies.viewmodels.RecipeListViewModel.QUERY_EXHAUSTED;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    @BindView(R.id.recipe_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_view)
    androidx.appcompat.widget.SearchView mSearchView;


    RecipeListViewModel mRecipeListViewModel;
    private RecipeRecyclerAdapter mAdapter;

    private static final String TAG = "RecipeListActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        initRecyclerView();
        initSearchView();
        setSupportActionBar(findViewById(R.id.toolbar));

        subscribeObservers();

    }

    public void subscribeObservers(){

        mRecipeListViewModel.getRecipes().observe(this, listResource -> {
            if(listResource != null){
                if(listResource.data != null){

                   switch (listResource.status){

                       case LOADING:{
                           if(mRecipeListViewModel.getPageNumber()>1){
                               mAdapter.displayLoading();
                           }else{
                               mAdapter.displayOnlyLoading();
                           }
                           break;
                       }
                       case ERROR:{
                            mAdapter.hideLoading();
                            mAdapter.setRecipes(listResource.data);
                            Toast.makeText(getApplicationContext(),listResource.message,Toast.LENGTH_SHORT).show();
                            if(listResource.message.equals(QUERY_EXHAUSTED)){
                                mAdapter.setQueryExhausted();
                            }
                           break;
                       }

                       case SUCCESS:{
                            mAdapter.hideLoading();
                            mAdapter.setRecipes(listResource.data);

                           break;
                       }


                   }

                }
            }
        });

        mRecipeListViewModel.getViewState().observe(this, viewState -> {
            if(viewState!=null){
                switch (viewState){
                    case RECIPES:
                        // show list of recipes
                        return;
                    case CATEGORIES:
                        displaySearchCategories();
                        return;

                }
            }
        });
    }

    private void searchRecipeApi(String query){
        mRecyclerView.smoothScrollToPosition(0);
        mRecipeListViewModel.searchRecipeApi(query,1);
        mSearchView.clearFocus();
    }

    private void displaySearchCategories() {
        mAdapter.displaySearchCategories();
    }


    private void initRecyclerView(){
        ViewPreloadSizeProvider<String> viewPreloader = new ViewPreloadSizeProvider<>();
        mAdapter = new RecipeRecyclerAdapter(this,initGlide(),viewPreloader);
        mRecyclerView.addItemDecoration(new VerticalSpacingItemDecorator(30));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerViewPreloader<String> preloader = new RecyclerViewPreloader<String>(Glide.with(this),mAdapter,viewPreloader,30);

        mRecyclerView.addOnScrollListener(preloader);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(recyclerView.canScrollVertically(1) && mRecipeListViewModel.getViewState().getValue() == RecipeListViewModel.ViewState.RECIPES){
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);



    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(getApplicationContext(),RecipeActivity.class);
        intent.putExtra("recipe",mAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        Log.d(TAG, "onCategoryClick: "+category);
        searchRecipeApi(category);
    }

    private void initSearchView(){
       mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
                searchRecipeApi(query);
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               return false;
           }
       });
    }

    private RequestManager initGlide(){
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background);
        return Glide.with(this).setDefaultRequestOptions(requestOptions);
    }

    @Override
    public void onBackPressed() {
        if(mRecipeListViewModel.getViewState().getValue() == RecipeListViewModel.ViewState.CATEGORIES){
            super.onBackPressed();
        }else{
            mRecipeListViewModel.cancelSearchRequest();
            mRecipeListViewModel.setCategoryView();
        }
    }
}
