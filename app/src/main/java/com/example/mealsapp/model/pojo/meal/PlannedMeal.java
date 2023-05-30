package com.example.mealsapp.model.pojo.meal;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PlannedMeal {
    @NonNull
    @PrimaryKey
    private String id;
    private Meal meal;
    private String date;

    public PlannedMeal() {}

    public PlannedMeal(@NonNull String id, Meal meal, String date) {
        this.id = id;
        this.meal = meal;
        this.date = date;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
