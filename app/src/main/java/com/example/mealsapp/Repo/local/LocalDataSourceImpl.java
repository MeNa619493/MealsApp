package com.example.mealsapp.Repo.local;

import android.content.Context;

import com.example.mealsapp.model.data.local.MealDao;
import com.example.mealsapp.model.data.local.PlannedMealDao;
import com.example.mealsapp.model.data.local.RoomDatabase;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.PlannedMeal;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class LocalDataSourceImpl implements LocalDataSource {
    private MealDao mealDao;
    private PlannedMealDao plannedMealDao;
    private static LocalDataSourceImpl instance;

    private LocalDataSourceImpl(Context context) {
        RoomDatabase roomDatabase = RoomDatabase.getInstance(context.getApplicationContext());
        this.mealDao = roomDatabase.mealDao();
        this.plannedMealDao = roomDatabase.plannedMealDao();
    }

    public static synchronized LocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new LocalDataSourceImpl(context);
        }
        return instance;
    }

    @Override
    public Completable insertMeal(Meal meal) {
        return mealDao.insertMeal(meal);
    }


    @Override
    public Completable insertAllMeals(List<Meal> meals) {
        return mealDao.insertAllMeals(meals);
    }

    @Override
    public Single<List<Meal>> getMeals() {
        return mealDao.getMeals();
    }

    @Override
    public Completable deleteMeal(Meal meal) {
        return mealDao.deleteMeal(meal);
    }

    @Override
    public Completable deleteAllMeals() {
        return mealDao.deleteAllMeals();
    }

    @Override
    public Completable insertPlannedMeal(PlannedMeal plannedMeal) {
        return plannedMealDao.insertPlannedMeal(plannedMeal);
    }

    @Override
    public Completable insertAllPlannedMeals(List<PlannedMeal> plannedMeals) {
        return plannedMealDao.insertAllPlannedMeals(plannedMeals);
    }

    @Override
    public Single<List<PlannedMeal>> getAllPlannedMeals() {
        return plannedMealDao.getAllPlannedMeals();
    }

    @Override
    public Completable deletePlannedMeal(PlannedMeal plannedMeal) {
        return plannedMealDao.deletePlannedMeal(plannedMeal);
    }

    @Override
    public Completable deleteAllPlannedMeal() {
        return plannedMealDao.deleteAllPlannedMeal();
    }
}
