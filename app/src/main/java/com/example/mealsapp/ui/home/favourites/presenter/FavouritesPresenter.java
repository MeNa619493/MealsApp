package com.example.mealsapp.ui.home.favourites.presenter;

import com.example.mealsapp.model.pojo.meal.Meal;

public interface FavouritesPresenter {
    void getFavouriteMeals();
    void deleteMeal(Meal meal);
    void onDestroy();
}
