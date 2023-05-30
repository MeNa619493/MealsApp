package com.example.mealsapp.ui.home.home.presenter;

import com.example.mealsapp.model.pojo.meal.Meal;

public interface HomePresenter {
    void getRandomMeal();
    void getSuggestionMeals();
    void addFavorite(Meal meal);
    void deleteMeal(Meal meal);
    boolean getIsLoggedInFlag();
    void onDestroy();
}
