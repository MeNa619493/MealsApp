package com.example.mealsapp.model.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.PlannedMeal;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface PlannedMealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertPlannedMeal(PlannedMeal plannedMeal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllPlannedMeals(List<PlannedMeal> plannedMeals);

    @Query("SELECT * FROM PlannedMeal")
    Single<List<PlannedMeal>> getAllPlannedMeals();

    @Delete
    Completable deletePlannedMeal(PlannedMeal plannedMeal);

    @Query("DELETE FROM PlannedMeal")
    Completable deleteAllPlannedMeal();
}
