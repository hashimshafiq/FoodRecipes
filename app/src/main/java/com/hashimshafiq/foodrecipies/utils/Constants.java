package com.hashimshafiq.foodrecipies.utils;

public class Constants {

    public static String BASE_URL = "https://www.food2fork.com";
    public static String API_KEY = "062cd1bfab843542562dfe774b760d66";
    public static int NETWORK_TIMEOUT = 10;
    public static int READ_TIMEOUT = 2;
    public static int WRITE_TIMEOUT = 2;

    public static int RECIPE_REFRESH_TIME = 60 * 60 * 24 * 30; // 30 days in second

    public static final String[] DEFAULT_SEARCH_CATEGORIES =
            {"Barbeque", "Breakfast", "Chicken", "Beef", "Brunch", "Dinner", "Wine", "Italian"};

    public static final String[] DEFAULT_SEARCH_CATEGORY_IMAGES =
            {
                    "barbeque",
                    "breakfast",
                    "chicken",
                    "beef",
                    "brunch",
                    "dinner",
                    "wine",
                    "italian"
            };
}
