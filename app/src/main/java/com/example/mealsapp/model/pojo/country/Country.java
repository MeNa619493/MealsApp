package com.example.mealsapp.model.pojo.country;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Country implements Parcelable {
    private String strArea;

    public Country(){}

    public Country(String strArea) {
        this.strArea = strArea;
    }

    protected Country(Parcel in) {
        strArea = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(strArea);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    @Override
    public String toString() {
        return "Country{" +
                "strArea='" + strArea + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(strArea, country.strArea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strArea);
    }
}
