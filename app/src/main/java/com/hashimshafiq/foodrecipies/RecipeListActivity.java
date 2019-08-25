package com.hashimshafiq.foodrecipies;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hashimshafiq.foodrecipies.adapters.RecipeRecyclerAdapter;
import com.hashimshafiq.foodrecipies.listeners.OnRecipeListener;
import com.hashimshafiq.foodrecipies.models.Recipe;
import com.hashimshafiq.foodrecipies.viewmodels.RecipeListViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    @BindView(R.id.recipe_list)
    RecyclerView mRecyclerView;

    RecipeListViewModel mRecipeListViewModel;
    private RecipeRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        initRecyclerView();

        subscribeObserver();

        testRequest();
    }

    private void subscribeObserver(){
        mRecipeListViewModel.getRecipes().observe(this, recipes -> {
            if(recipes != null){
                mAdapter.setRecipes(recipes);
            }
        });
    }

    private void testRequest(){
        searchRecipeApi("chicken breast",1);

    }

    public void searchRecipeApi(String query, int pageNumber){
        mRecipeListViewModel.searchRecipeApi(query,pageNumber);
    }

    private void initRecyclerView(){
        mAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {

    }
}
