package com.example.mealsapp.ui.home.searchbytype.view;

import com.example.mealsapp.model.pojo.meal.Meal;
import java.util.List;

public interface SearchByTypeView {
    void showMeals(List<Meal> meals);
    void showMealsSearchResult(List<Meal> meals);
    void showError(Throwable throwable);
    void mealAddedToFavoriteSuccessfully(Meal meal);
    void mealAddedToFavoriteFailure();
    void mealDeletedSuccessfully(Meal meal);
    void mealDeletedFailure();
}
