package com.example.mealsapp.model.pojo.meal;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class MealResponse implements Parcelable {
    private List<Meal> meals;

    public MealResponse() {}

    public MealResponse(List<Meal> meals) {
        this.meals = meals;
    }

    public List<Meal> getMeals() {
        return this.meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    protected MealResponse(Parcel in) {
    }

    public static final Creator<MealResponse> CREATOR = new Creator<MealResponse>() {
        @Override
        public MealResponse createFromParcel(Parcel in) {
            return new MealResponse(in);
        }

        @Override
        public MealResponse[] newArray(int size) {
            return new MealResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeList(meals);
    }
}
