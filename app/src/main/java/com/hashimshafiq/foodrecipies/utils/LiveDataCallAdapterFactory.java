package com.hashimshafiq.foodrecipies.utils;

import androidx.lifecycle.LiveData;

import com.hashimshafiq.foodrecipies.requests.ApiResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {



    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

        //check 1
        // Make sure the CallAdapter is returning a type of LiveData
        if(CallAdapter.Factory.getRawType(returnType) != LiveData.class){
            return null;
        }


        //Check 2
        // Type that LiveData is wrapping
        Type observableType = CallAdapter.Factory.getParameterUpperBound(0,(ParameterizedType) returnType);

        // check if it is of ApiResponse
        Type rawObservableType = CallAdapter.Factory.getRawType(observableType);
        if(rawObservableType != ApiResponse.class){
            throw  new IllegalArgumentException("Type must be defined resource");
        }


        // Check 3
        //Check if ApiResponse is parametrized means Does ApiResponse<T> exist?
        if(!(observableType instanceof ParameterizedType)){
            throw new IllegalArgumentException("resource must be parametrized");
        }

        Type bodyType = CallAdapter.Factory.getParameterUpperBound(0,(ParameterizedType) observableType);
        return new LiveDataCallAdapter<Type>(bodyType);
    }
}
