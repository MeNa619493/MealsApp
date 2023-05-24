package com.example.mealsapp.model.pojo.ingredient;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Ingredient implements Parcelable {
    private String idIngredient;
    private String strIngredient;
    private String strDescription;

    public Ingredient(){}

    public Ingredient(String idIngredient, String strIngredient, String strDescription) {
        this.idIngredient = idIngredient;
        this.strIngredient = strIngredient;
        this.strDescription = strDescription;
    }

    protected Ingredient(Parcel in) {
        idIngredient = in.readString();
        strIngredient = in.readString();
        strDescription = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idIngredient);
        dest.writeString(strIngredient);
        dest.writeString(strDescription);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(String idIngredient) {
        this.idIngredient = idIngredient;
    }

    public String getStrIngredient() {
        return strIngredient;
    }

    public void setStrIngredient(String strIngredient) {
        this.strIngredient = strIngredient;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(idIngredient, that.idIngredient) && Objects.equals(strIngredient, that.strIngredient) && Objects.equals(strDescription, that.strDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIngredient, strIngredient, strDescription);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "idIngredient='" + idIngredient + '\'' +
                ", strIngredient='" + strIngredient + '\'' +
                ", strDescription='" + strDescription + '\'' +
                '}';
    }
}
