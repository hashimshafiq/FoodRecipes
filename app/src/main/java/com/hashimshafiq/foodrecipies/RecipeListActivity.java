package com.hashimshafiq.foodrecipies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hashimshafiq.foodrecipies.adapters.RecipeRecyclerAdapter;
import com.hashimshafiq.foodrecipies.listeners.OnRecipeListener;
import com.hashimshafiq.foodrecipies.utils.VerticalSpacingItemDecorator;
import com.hashimshafiq.foodrecipies.viewmodels.RecipeListViewModel;

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

        subscribeObserver();

        initSearchView();

        if(!mRecipeListViewModel.IsViewingRecipes()){
            displaySearchCategories();
        }

        setSupportActionBar(findViewById(R.id.toolbar));

    }

    private void subscribeObserver(){
        mRecipeListViewModel.getRecipes().observe(this, recipes -> {
            if(recipes != null){
                if(mRecipeListViewModel.IsViewingRecipes()){
                    mRecipeListViewModel.setIsPerformingQuery(false);
                    mAdapter.setRecipes(recipes);
                }

            }
        });

        mRecipeListViewModel.isQueryExhausted().observe(this, aBoolean -> {
            if(aBoolean)
                mAdapter.setQueryExhausted();
        });
    }

    public void searchRecipeApi(String query, int pageNumber){
        mRecipeListViewModel.searchRecipeApi(query,pageNumber);
    }

    private void initRecyclerView(){
        mAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView.addItemDecoration(new VerticalSpacingItemDecorator(30));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!mRecyclerView.canScrollVertically(1)){
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });

    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(getApplicationContext(),RecipeActivity.class);
        intent.putExtra("recipe",mAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        mAdapter.displayLoading();
        mRecipeListViewModel.searchRecipeApi(category,1);
    }

    private void initSearchView(){
       mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               mAdapter.displayLoading();
               mRecipeListViewModel.searchRecipeApi(query,1);
               mSearchView.clearFocus();
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               return false;
           }
       });
    }

    private void displaySearchCategories(){
        mRecipeListViewModel.setIsViewingRecipes(false);
        mAdapter.displaySearchCategories();
        mSearchView.clearFocus();
    }

    @Override
    public void onBackPressed() {
        if(mRecipeListViewModel.onBackPressed()){
            super.onBackPressed();
        }else{
            displaySearchCategories();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_categories) {
            displaySearchCategories();
        }

        return super.onOptionsItemSelected(item);
    }
}
