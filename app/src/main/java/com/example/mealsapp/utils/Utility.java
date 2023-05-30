package com.example.mealsapp.utils;

import com.example.mealsapp.model.pojo.meal.Meal;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

public class Utility {

    public static ArrayList<String> getMealIngredients(Meal meal) {
        ArrayList<String> ingredients = new ArrayList<>();
        addStringToArrayList(ingredients, meal.getStrIngredient1());
        addStringToArrayList(ingredients, meal.getStrIngredient2());
        addStringToArrayList(ingredients, meal.getStrIngredient3());
        addStringToArrayList(ingredients, meal.getStrIngredient4());
        addStringToArrayList(ingredients, meal.getStrIngredient5());
        addStringToArrayList(ingredients, meal.getStrIngredient6());
        addStringToArrayList(ingredients, meal.getStrIngredient7());
        addStringToArrayList(ingredients, meal.getStrIngredient8());
        addStringToArrayList(ingredients, meal.getStrIngredient9());
        addStringToArrayList(ingredients, meal.getStrIngredient10());
        addStringToArrayList(ingredients, meal.getStrIngredient11());
        addStringToArrayList(ingredients, meal.getStrIngredient12());
        addStringToArrayList(ingredients, meal.getStrIngredient13());
        addStringToArrayList(ingredients, meal.getStrIngredient14());
        addStringToArrayList(ingredients, meal.getStrIngredient15());
        addStringToArrayList(ingredients, meal.getStrIngredient16());
        addStringToArrayList(ingredients, meal.getStrIngredient17());
        addStringToArrayList(ingredients, meal.getStrIngredient18());
        addStringToArrayList(ingredients, meal.getStrIngredient19());
        addStringToArrayList(ingredients, meal.getStrIngredient20());
        return ingredients;
    }

    private static void addStringToArrayList(ArrayList<String> ingredients, String ingredient) {
        if (ingredient != null && !ingredient.isEmpty()) {
            ingredients.add(ingredient);
        }
    }

}
