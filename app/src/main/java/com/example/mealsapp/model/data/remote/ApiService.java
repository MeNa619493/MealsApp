package com.example.mealsapp.model.data.remote;

import com.example.mealsapp.model.pojo.category.CategoryResponse;
import com.example.mealsapp.model.pojo.country.CountryResponse;
import com.example.mealsapp.model.pojo.ingredient.IngredientResponse;
import com.example.mealsapp.model.pojo.meal.MealResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    @GET("lookup.php")
    Single<MealResponse> getMealById(@Query("i") String id);

    @GET("search.php")
    Single<MealResponse> searchByName(@Query("s") String mealName);

    @GET("list.php?c=list")
    Single<CategoryResponse> getCategories();

    @GET("list.php?a=list")
    Single<CountryResponse> getCountries();

    @GET("list.php?i=list")
    Single<IngredientResponse> getIngredients();

    @GET("filter.php")
    Single<MealResponse> searchByCategory(@Query("c") String categoryName);

    @GET("filter.php")
    Single<MealResponse> searchByCountry(@Query("a") String countryName);

    @GET("filter.php")
    Single<MealResponse> searchByIngredient(@Query("i") String ingredientName);
}
