package com.example.mealsapp.ui.home.calender.presenter;

import com.example.mealsapp.model.pojo.meal.PlannedMeal;

public interface CalendarPresenter {
    void getAllPlannedMeals(String date);
    void deleteMeal(PlannedMeal meal);
    void onDestroy();
}
