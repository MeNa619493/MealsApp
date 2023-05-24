package com.example.mealsapp.model.pojo.category;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class CategoryResponse implements Parcelable {

    private List<Category> meals;

    public CategoryResponse() {}

    public CategoryResponse(List<Category> meals) {
        this.meals = meals;
    }

    protected CategoryResponse(Parcel in) {
        meals = in.createTypedArrayList(Category.CREATOR);
    }

    public List<Category> getMeals() {
        return this.meals;
    }

    public void setMeals(List<Category> meals) {
        this.meals = meals;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(meals);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryResponse> CREATOR = new Creator<CategoryResponse>() {
        @Override
        public CategoryResponse createFromParcel(Parcel in) {
            return new CategoryResponse(in);
        }

        @Override
        public CategoryResponse[] newArray(int size) {
            return new CategoryResponse[size];
        }
    };

}
