package com.hashimshafiq.foodrecipies.viewholders;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.hashimshafiq.foodrecipies.R;
import com.hashimshafiq.foodrecipies.listeners.OnRecipeListener;
import com.hashimshafiq.foodrecipies.models.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.category_image)
    public CircleImageView mCategoryImage;
    @BindView(R.id.category_title)
    public TextView mCategoryTitle;
    OnRecipeListener mOnRecipeListener;
    RequestManager requestManager;

    public CategoryViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener, RequestManager requestManager) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.mOnRecipeListener = onRecipeListener;
        this.requestManager = requestManager;

    }


    @OnClick(R.id.card_view) void onClickCategory(){
        mOnRecipeListener.onCategoryClick(mCategoryTitle.getText().toString());
    }


    public void onBind(Recipe recipe){
        Uri uri = Uri.parse("android.resource://com.hashimshafiq.foodrecipies/drawable/"+recipe.getImage_url());

        requestManager
                .load(uri)
                .into(mCategoryImage);

        mCategoryTitle.setText(recipe.getTitle());
    }
}