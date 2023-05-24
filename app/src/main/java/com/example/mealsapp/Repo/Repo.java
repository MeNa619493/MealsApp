package com.example.mealsapp.Repo;

import com.example.mealsapp.Repo.local.LocalDataSource;
import com.example.mealsapp.Repo.remote.RemoteDataSource;
import com.example.mealsapp.model.pojo.category.CategoryResponse;
import com.example.mealsapp.model.pojo.country.CountryResponse;
import com.example.mealsapp.model.pojo.ingredient.IngredientResponse;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.MealResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class Repo {
    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;
    private static Repo instance;

    public Repo(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public static synchronized Repo getInstance(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        if (instance == null) {
            instance = new Repo(localDataSource, remoteDataSource);
        }
        return instance;
    }

    public Single<MealResponse> getRandomMeal() {
        return remoteDataSource.getRandomMeal();
    }

    public Single<MealResponse> getSuggestionMeals(char c) {
        return remoteDataSource.getYouMightLikeMeal(c);
    }

    public Single<CategoryResponse> getCategories() {
        return remoteDataSource.getCategories();
    }

    public Single<CountryResponse> getCountries() {
        return remoteDataSource.getCountries();
    }

    public Single<IngredientResponse> getIngredients() {
        return remoteDataSource.getIngredients();
    }

    public Single<MealResponse> searchByCategory(String categoryName) {
        return remoteDataSource.searchByCategory(categoryName);
    }

    public Single<MealResponse> searchByCountry(String countryName) {
        return remoteDataSource.searchByCountry(countryName);
    }

    public Single<MealResponse> searchByIngredient(String ingredientName) {
        return remoteDataSource.searchByIngredient(ingredientName);
    }

    public Completable addFavorite(Meal meal) {
        return localDataSource.insertMeal(meal);
    }

    public Single<List<Meal>> getFavouriteMeals() {
        return localDataSource.getMeals();
    }

    public Completable deleteFavorite(Meal meal) {
        return localDataSource.deleteMeal(meal);
    }
}
