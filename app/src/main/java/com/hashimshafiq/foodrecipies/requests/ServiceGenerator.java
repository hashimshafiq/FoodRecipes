package com.hashimshafiq.foodrecipies.requests;

import com.hashimshafiq.foodrecipies.utils.Constants;
import com.hashimshafiq.foodrecipies.utils.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.MINUTES)
            .readTimeout(Constants.READ_TIMEOUT,TimeUnit.MINUTES)
            .writeTimeout(Constants.WRITE_TIMEOUT,TimeUnit.MINUTES)
            .retryOnConnectionFailure(false)
            .build();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(new LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create());


    private static Retrofit retrofit = retrofitBuilder.build();

    private static RecipeApi recipeApi = retrofit.create(RecipeApi.class);

    public static RecipeApi getRecipeApi(){
        return recipeApi;
    }
}
