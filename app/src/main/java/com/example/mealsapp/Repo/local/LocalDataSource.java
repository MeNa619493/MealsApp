package com.example.mealsapp.Repo.local;

import com.example.mealsapp.model.pojo.meal.Meal;
import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface LocalDataSource {
    Completable insertMeal(Meal meal);
    Single<List<Meal>> getMeals();
    Completable deleteMeal(Meal meal);
    Completable deleteAllMeals();
}
