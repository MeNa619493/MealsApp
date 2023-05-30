package com.example.mealsapp.ui.home.searchbytype.presenter;

import com.example.mealsapp.model.pojo.meal.Meal;

public interface SearchByTypePresenter {
    void getMealsByCategory(String categoryName);
    void getMealsByCountry(String countryName);
    void getMealsByIngredient(String ingredientName);
    void searchMealByName(String name);
    void addFavorite(Meal meal);
    void deleteMeal(Meal meal);
    boolean getIsLoggedInFlag();
    void onDestroy();
}
