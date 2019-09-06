package com.hashimshafiq.foodrecipies.persistence;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Converters {


    @TypeConverter
    public static String[] fromString(String value){
        Type type = new TypeToken<String[]>(){}.getType();
        return new Gson().fromJson(value,type);
    }


    @TypeConverter
    public static String fromArryList(String[] list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;

    }
}
