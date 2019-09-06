package com.hashimshafiq.foodrecipies.persistence;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.hashimshafiq.foodrecipies.models.Recipe;

@Database(entities = {Recipe.class},version = 1)
@TypeConverters({Converters.class})
public abstract class RecipesDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "recipes_db";
    public static RecipesDatabase instance;

    public static RecipesDatabase getInstance(final Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context,RecipesDatabase.class,DATABASE_NAME).build();
        }

        return instance;
    }

    public abstract RecipeDao getRecipeDao();
}
