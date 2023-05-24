package com.example.mealsapp.model.pojo.category;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Category implements Parcelable {
    private String strCategory;

    public Category() {}

    public Category(String strCategory) {
        this.strCategory = strCategory;
    }

    protected Category(Parcel in) {
        strCategory = in.readString();
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(strCategory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(strCategory, category.strCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strCategory);
    }

    @Override
    public String toString() {
        return "Category{" +
                "strCategory='" + strCategory + '\'' +
                '}';
    }
}
