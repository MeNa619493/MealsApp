package com.example.mealsapp.ui.home.home.view;

import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.MealResponse;

import java.util.List;

public interface HomeView {
    void showMeal(Meal meal);
    void showSuggestionMeals(List<Meal> meals);
    void showError(Throwable throwable);
    void mealAddedToFavoriteSuccessfully(Meal meal);
    void mealAddedToFavoriteFailure();
    void mealDeletedSuccessfully(Meal meal);
    void mealDeletedFailure();
}