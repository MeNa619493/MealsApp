package com.example.mealsapp.ui.home.search.presenter;

import com.example.mealsapp.model.pojo.meal.Meal;

public interface SearchPresenter {
    void searchByName(String name);
    void getCategories();
    void getCountries();
    void getIngredients();
    void addFavorite(Meal meal);
    void deleteMeal(Meal meal);
    void onDestroy();
}
