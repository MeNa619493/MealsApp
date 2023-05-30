package com.example.mealsapp.model.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.mealsapp.model.pojo.meal.Meal;
import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMeal(Meal meal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllMeals(List<Meal> meals);

    @Query("SELECT * FROM Meal")
    Single<List<Meal>> getMeals();

    @Delete
    Completable deleteMeal(Meal meal);

    @Query("DELETE FROM Meal")
    Completable deleteAllMeals();
}
