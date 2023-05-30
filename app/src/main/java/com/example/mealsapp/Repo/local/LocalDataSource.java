package com.example.mealsapp.Repo.local;

import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.PlannedMeal;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface LocalDataSource {
    Completable insertMeal(Meal meal);

    Completable insertAllMeals(List<Meal> meals);

    Single<List<Meal>> getMeals();

    Completable deleteMeal(Meal meal);

    Completable deleteAllMeals();

    Completable insertPlannedMeal(PlannedMeal plannedMeal);

    Completable insertAllPlannedMeals(List<PlannedMeal> plannedMeals);

    Single<List<PlannedMeal>> getAllPlannedMeals();

    Completable deletePlannedMeal(PlannedMeal plannedMeal);

    Completable deleteAllPlannedMeal();
}
