package com.example.mealsapp.ui.home.details.presnter;

import com.example.mealsapp.model.pojo.meal.Meal;

public interface DetailsPresenter {
    void addFavorite(Meal meal);
    void deleteMeal(Meal meal);
    void onDestroy();
}
