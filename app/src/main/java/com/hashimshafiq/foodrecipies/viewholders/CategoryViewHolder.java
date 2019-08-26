package com.hashimshafiq.foodrecipies.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hashimshafiq.foodrecipies.R;
import com.hashimshafiq.foodrecipies.listeners.OnRecipeListener;

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

    public CategoryViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.mOnRecipeListener = onRecipeListener;

    }


    @OnClick(R.id.card_view) void onClickCategory(){
        mOnRecipeListener.onCategoryClick(mCategoryTitle.getText().toString());
    }
}