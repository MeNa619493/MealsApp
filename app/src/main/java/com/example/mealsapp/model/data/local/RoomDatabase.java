package com.example.mealsapp.model.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;

import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.PlannedMeal;

@Database(entities = {Meal.class , PlannedMeal.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static volatile RoomDatabase instance;
    public abstract MealDao mealDao();
    public abstract PlannedMealDao plannedMealDao();

    public static RoomDatabase getInstance(Context context){
        if(instance == null)
        {
            synchronized (RoomDatabase.class){
                if(instance == null)
                {
                    instance = Room.databaseBuilder(context,
                                    RoomDatabase.class,
                                    "Meals Database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
