package com.example.mealsapp.model.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import com.example.mealsapp.model.pojo.meal.Meal;

@Database(entities = Meal.class, version = 1)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static volatile RoomDatabase instance;
    public abstract MealDao mealDao();

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
