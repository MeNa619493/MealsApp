package com.example.mealsapp.ui.home.search.view;

import com.example.mealsapp.model.pojo.category.Category;
import com.example.mealsapp.model.pojo.country.Country;
import com.example.mealsapp.model.pojo.ingredient.Ingredient;
import com.example.mealsapp.model.pojo.meal.Meal;

import java.util.List;

public interface SearchView {
    void showSearchResultSuccess(List<Meal> meals);
    void showSearchResultError(Throwable throwable);
    void showCategories(List<Category> categories);
    void showIngredients(List<Ingredient> ingredients);
    void showCountries(List<Country> countries);
    void showError(Throwable throwable);
    void mealAddedToFavoriteSuccessfully(Meal meal);
    void mealAddedToFavoriteFailure();
    void mealDeletedSuccessfully(Meal meal);
    void mealDeletedFailure();
}
