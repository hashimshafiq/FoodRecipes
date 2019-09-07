package com.hashimshafiq.foodrecipies.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.hashimshafiq.foodrecipies.R;
import com.hashimshafiq.foodrecipies.listeners.OnRecipeListener;
import com.hashimshafiq.foodrecipies.models.Recipe;
import com.hashimshafiq.foodrecipies.utils.Constants;
import com.hashimshafiq.foodrecipies.viewholders.CategoryViewHolder;
import com.hashimshafiq.foodrecipies.viewholders.LoadingViewHolder;
import com.hashimshafiq.foodrecipies.viewholders.RecipeViewHolder;
import com.hashimshafiq.foodrecipies.viewholders.SearchExhaustedViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;


    private List<Recipe> mRecipes;
    private OnRecipeListener mOnRecipeListener;
    private RequestManager requestManager;

    public RecipeRecyclerAdapter( OnRecipeListener mOnRecipeListener, RequestManager requestManager) {
        this.mRecipes = new ArrayList<>();
        this.mOnRecipeListener = mOnRecipeListener;
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){

            default:
            case RECIPE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item,parent,false);
                return new RecipeViewHolder(view,mOnRecipeListener,requestManager);
            case LOADING_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_list_item,parent,false);
                return new LoadingViewHolder(view);
            case EXHAUSTED_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_exhausted,parent,false);
                return new SearchExhaustedViewHolder(view);
            case CATEGORY_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_list_item,parent,false);
                return new CategoryViewHolder(view,mOnRecipeListener,requestManager);


        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);

        if(itemViewType==RECIPE_TYPE){
            ((RecipeViewHolder) holder).onBind(mRecipes.get(position));
        }else if(itemViewType==CATEGORY_TYPE){
            ((CategoryViewHolder) holder).onBind(mRecipes.get(position));
        }


    }

    @Override
    public int getItemViewType(int position) {
        if(mRecipes.get(position).getTitle().equalsIgnoreCase("LOADING...")){
            return LOADING_TYPE;
        }if(mRecipes.get(position).getTitle().equalsIgnoreCase("EXHAUSTED...")){
            return EXHAUSTED_TYPE;
        }else if(mRecipes.get(position).getSocial_rank()==-1){
            return CATEGORY_TYPE;
        } else{
            return RECIPE_TYPE;
        }
    }


    public void hideLoading(){
        if(isLoading()){
            if(mRecipes.get(0).getTitle().equals("LOADING...")){
                mRecipes.remove(0);
            }else if(mRecipes.get(mRecipes.size()-1).getTitle().equals("LOADING...")){
                mRecipes.remove(mRecipes.size()-1);
            }
            notifyDataSetChanged();
        }
    }

    public void displayOnlyLoading(){
        clearRecipesList();
        Recipe recipe = new Recipe();
        recipe.setTitle("LOADING...");
        mRecipes.add(recipe);
        notifyDataSetChanged();
    }

    private void clearRecipesList(){
        if(mRecipes == null){
            mRecipes = new ArrayList<>();
        }else{
            mRecipes.clear();
        }
    }

    public void setQueryExhausted(){
        hideLoading();
        Recipe recipe = new Recipe();
        recipe.setTitle("EXHAUSTED...");
        mRecipes.add(recipe);
        notifyDataSetChanged();


    }

    public void displayLoading(){
        if(mRecipes == null){
            mRecipes = new ArrayList<>();
        }

        if(!isLoading()){
            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            mRecipes.add(recipe);
            notifyDataSetChanged();

        }
    }

    private boolean isLoading(){
        if(mRecipes.size()>0){
            if(mRecipes.get(mRecipes.size()-1).getTitle().equalsIgnoreCase("LOADING...")){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }


    public void setRecipes(List<Recipe> recipes){
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public void displaySearchCategories(){
        List<Recipe> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORIES.length; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setSocial_rank(-1);
            categories.add(recipe);
        }
        mRecipes = categories;
        notifyDataSetChanged();
    }


    public Recipe getSelectedRecipe(int position){
        if(mRecipes != null){
            if(mRecipes.size()>0){
                return mRecipes.get(position);
            }
        }
        return null;
    }
}
