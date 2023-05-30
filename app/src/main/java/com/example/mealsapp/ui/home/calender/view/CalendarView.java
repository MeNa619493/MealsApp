package com.example.mealsapp.ui.home.calender.view;

import com.example.mealsapp.model.pojo.meal.PlannedMeal;

import java.util.List;

public interface CalendarView {
    void showPlannedMeals(List<PlannedMeal> meals, String date);
    void showError(Throwable throwable);
    void mealDeletedSuccessfully(PlannedMeal meal);
    void mealDeletedFailure();
}
