package com.example.mealsapp.Repo.remote;

import com.example.mealsapp.model.pojo.category.CategoryResponse;
import com.example.mealsapp.model.pojo.country.CountryResponse;
import com.example.mealsapp.model.pojo.ingredient.IngredientResponse;
import com.example.mealsapp.model.pojo.meal.MealResponse;
import com.example.mealsapp.model.data.remote.ApiService;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient implements RemoteDataSource {

    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static ApiService retrofit;
    private static ApiClient instance = null;

    private ApiClient() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = new ApiClient();
                }
            }
        }
        return instance;
    }

    public Single<MealResponse> getRandomMeal() {
        return retrofit.getRandomMeal();
    }

    public Single<MealResponse> getYouMightLikeMeal(char c) {
        return retrofit.searchByName(String.valueOf(c));
    }

    public Single<CategoryResponse> getCategories() {
        return retrofit.getCategories();
    }

    public Single<CountryResponse> getCountries() {
        return retrofit.getCountries();
    }

    public Single<IngredientResponse> getIngredients() {
        return retrofit.getIngredients();
    }

    @Override
    public Single<MealResponse> searchByCategory(String categoryName) {
        return retrofit.searchByCategory(categoryName);
    }

    @Override
    public Single<MealResponse> searchByCountry(String countryName) {
        return retrofit.searchByCountry(countryName);
    }

    @Override
    public Single<MealResponse> searchByIngredient(String ingredientName) {
        return retrofit.searchByIngredient(ingredientName);
    }

    public Single<MealResponse> searchByName(String name) {
        return retrofit.searchByName(name);
    }
}
