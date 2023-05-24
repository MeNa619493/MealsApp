package com.example.mealsapp.ui.home.searchbyingredients.presenter;

public interface SearchByIngredientsPresenter {
    void searchIngredientsByName(String name);
    void getIngredients();
    void onDestroy();
}
