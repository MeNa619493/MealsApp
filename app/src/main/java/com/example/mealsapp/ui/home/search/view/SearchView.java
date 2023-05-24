package com.example.mealsapp.ui.home.search.view;

import com.example.mealsapp.model.pojo.category.CategoryResponse;
import com.example.mealsapp.model.pojo.country.CountryResponse;
import com.example.mealsapp.model.pojo.ingredient.IngredientResponse;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.MealResponse;

public interface SearchView {
    void showSearchResultSuccess(MealResponse mealResponse);
    void showCategories(CategoryResponse categoryResponse);
    void showIngredients(IngredientResponse ingredientResponse);
    void showCountries(CountryResponse countryResponse);
    void showError(Throwable throwable);
    void mealAddedToFavoriteSuccessfully(Meal meal);
    void mealAddedToFavoriteFailure();
    void mealDeletedSuccessfully(Meal meal);
    void mealDeletedFailure();
}
