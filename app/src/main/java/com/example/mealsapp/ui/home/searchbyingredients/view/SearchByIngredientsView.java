package com.example.mealsapp.ui.home.searchbyingredients.view;

import com.example.mealsapp.model.pojo.ingredient.Ingredient;
import com.example.mealsapp.model.pojo.ingredient.IngredientResponse;
import java.util.List;

public interface SearchByIngredientsView {
    void showIngredients(IngredientResponse ingredientResponse);
    void showIngredientsSearchResult(List<Ingredient> ingredients);
    void showError(Throwable throwable);
}
