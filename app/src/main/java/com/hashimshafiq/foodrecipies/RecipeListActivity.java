package com.hashimshafiq.foodrecipies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

                    Testing.printRecipes(listResource.data,"data");

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
        Log.d(TAG, "searchRecipeApi: "+query);
        mRecipeListViewModel.searchRecipeApi(query,1);

    }

    private void displaySearchCategories() {
        mAdapter.displaySearchCategories();
    }


    private void initRecyclerView(){
        mAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView.addItemDecoration(new VerticalSpacingItemDecorator(30));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


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


}
