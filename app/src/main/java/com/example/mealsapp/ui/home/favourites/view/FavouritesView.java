package com.example.mealsapp.ui.home.favourites.view;

import com.example.mealsapp.model.pojo.meal.Meal;
import java.util.List;

public interface FavouritesView {
    void showFavouriteMeals(List<Meal> meals);
    void showError(Throwable throwable);
    void mealDeletedSuccessfully(Meal meal);
    void mealDeletedFailure();
}
