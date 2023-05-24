package com.example.mealsapp.Repo.remote;

import com.example.mealsapp.model.pojo.category.CategoryResponse;
import com.example.mealsapp.model.pojo.country.CountryResponse;
import com.example.mealsapp.model.pojo.ingredient.IngredientResponse;
import com.example.mealsapp.model.pojo.meal.MealResponse;

import io.reactivex.Single;

public interface RemoteDataSource {
    Single<MealResponse> getRandomMeal();

    Single<MealResponse> getYouMightLikeMeal(char c);

    Single<CategoryResponse> getCategories();

    Single<CountryResponse> getCountries();

    Single<IngredientResponse> getIngredients();

    Single<MealResponse> searchByCategory(String categoryName);

    Single<MealResponse> searchByCountry(String countryName);

    Single<MealResponse> searchByIngredient(String ingredientName);
}
