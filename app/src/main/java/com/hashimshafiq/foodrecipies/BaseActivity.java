package com.hashimshafiq.foodrecipies;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class BaseActivity extends AppCompatActivity {

    public ProgressBar mProgressBar;

    @Override
    public void setContentView(int layoutResID) {

        ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base,null);
        FrameLayout frameLayout = constraintLayout.findViewById(R.id.activity_content);
        mProgressBar = constraintLayout.findViewById(R.id.progressbar);
        getLayoutInflater().inflate(layoutResID,frameLayout,true);
        super.setContentView(layoutResID);
    }


    public void showProgressBar(boolean visibility){
        mProgressBar.setVisibility(visibility? View.VISIBLE: View.INVISIBLE);

    }
}
