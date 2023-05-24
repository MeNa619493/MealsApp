package com.example.mealsapp.model.pojo.country;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class CountryResponse implements Parcelable{
    private List<Country> meals;

    public CountryResponse() {}

    public CountryResponse(List<Country> meals) {
        this.meals = meals;
    }

    protected CountryResponse(Parcel in) {
        meals = in.createTypedArrayList(Country.CREATOR);
    }

    public List<Country> getMeals() {
        return this.meals;
    }

    public void setMeals(List<Country> meals) {
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

    public static final Parcelable.Creator<CountryResponse> CREATOR = new Parcelable.Creator<CountryResponse>() {
        @Override
        public CountryResponse createFromParcel(Parcel in) {
            return new CountryResponse(in);
        }

        @Override
        public CountryResponse[] newArray(int size) {
            return new CountryResponse[size];
        }
    };
}
