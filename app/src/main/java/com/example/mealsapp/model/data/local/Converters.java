package com.example.mealsapp.model.data.local;

import androidx.room.TypeConverter;

import com.example.mealsapp.model.pojo.meal.Meal;
import com.google.gson.Gson;

public class Converters {
    @TypeConverter
    public String fromMealToString(Meal meal) {
        return new Gson().toJson(meal);
    }

    @TypeConverter
    public Meal fromStringToMeal(String string) {
        return new Gson().fromJson(string, Meal.class);
    }
}
