package com.hashimshafiq.foodrecipies.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hashimshafiq.foodrecipies.R;
import com.hashimshafiq.foodrecipies.listeners.OnRecipeListener;
import com.hashimshafiq.foodrecipies.models.Recipe;
import com.hashimshafiq.foodrecipies.utils.Constants;
import com.hashimshafiq.foodrecipies.viewholders.CategoryViewHolder;
import com.hashimshafiq.foodrecipies.viewholders.LoadingViewHolder;
import com.hashimshafiq.foodrecipies.viewholders.RecipeViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;


    private List<Recipe> mRecipes;
    private OnRecipeListener mOnRecipeListener;

    public RecipeRecyclerAdapter( OnRecipeListener mOnRecipeListener) {
        this.mRecipes = new ArrayList<>();
        this.mOnRecipeListener = mOnRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){

            default:
            case RECIPE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item,parent,false);
                return new RecipeViewHolder(view,mOnRecipeListener);
            case LOADING_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_list_item,parent,false);
                return new LoadingViewHolder(view);
            case CATEGORY_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_list_item,parent,false);
                return new CategoryViewHolder(view,mOnRecipeListener);


        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);

        if(itemViewType==RECIPE_TYPE){
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(mRecipes.get(position).getImage_url())
                    .into(((RecipeViewHolder)holder).mRecipeImageView);

            ((RecipeViewHolder)holder).mRecipeTitle.setText(mRecipes.get(position).getTitle());
            ((RecipeViewHolder)holder).mRecipePublisher.setText(mRecipes.get(position).getPublisher());
            ((RecipeViewHolder)holder).mRecipeSocialScore.setText(String.valueOf(Math.round(mRecipes.get(position).getSocial_rank())));
        }else if(itemViewType==CATEGORY_TYPE){
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Uri uri = Uri.parse("android.resource://com.hashimshafiq.foodrecipies/drawable/"+mRecipes.get(position).getImage_url());

            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(uri)
                    .into(((CategoryViewHolder)holder).mCategoryImage);

            ((CategoryViewHolder)holder).mCategoryTitle.setText(mRecipes.get(position).getTitle());
        }


    }

    @Override
    public int getItemViewType(int position) {
        if(mRecipes.get(position).getTitle().equalsIgnoreCase("LOADING...")){
            return LOADING_TYPE;
        }else if(mRecipes.get(position).getSocial_rank()==-1){
            return CATEGORY_TYPE;
        } else{
            return RECIPE_TYPE;
        }
    }

    public void displayLoading(){
        if(!isLoading()){
            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            List<Recipe> list = new ArrayList<>();
            list.add(recipe);
            mRecipes = list;
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
}
