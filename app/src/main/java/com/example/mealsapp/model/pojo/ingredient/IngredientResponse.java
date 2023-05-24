package com.example.mealsapp.model.pojo.ingredient;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class IngredientResponse implements Parcelable {
    private List<Ingredient> meals;

    public IngredientResponse(){}

    public IngredientResponse(List<Ingredient> meals) {
        this.meals = meals;
    }

    protected IngredientResponse(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IngredientResponse> CREATOR = new Creator<IngredientResponse>() {
        @Override
        public IngredientResponse createFromParcel(Parcel in) {
            return new IngredientResponse(in);
        }

        @Override
        public IngredientResponse[] newArray(int size) {
            return new IngredientResponse[size];
        }
    };

    public List<Ingredient> getMeals() {
        return meals;
    }

    public void setMeals(List<Ingredient> meals) {
        this.meals = meals;
    }
}
