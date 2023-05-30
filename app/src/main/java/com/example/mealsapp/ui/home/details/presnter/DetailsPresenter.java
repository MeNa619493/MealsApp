package com.example.mealsapp.ui.home.details.presnter;

import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.PlannedMeal;

public interface DetailsPresenter {
    void  getMealById(String id);
    void addFavorite(Meal meal);
    void deleteMeal(Meal meal);
    void addPlannedMeal(PlannedMeal plannedMeal);
    boolean getIsLoggedInFlag();
    void onDestroy();
}
