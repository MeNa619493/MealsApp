package com.example.mealsapp.ui.home.details.view;

import com.example.mealsapp.model.pojo.meal.Meal;

public interface DetailsView {
    void assignMeal(Meal meal);
    void assignMealFailure(String message);
    void mealAddedToFavoriteSuccessfully(Meal meal);
    void mealAddedToFavoriteFailure();
    void mealDeletedSuccessfully(Meal meal);
    void mealDeletedFailure();
    void mealAddedToWeekPlanSuccessfully();
    void mealAddedToWeekPlanFailure();
}
