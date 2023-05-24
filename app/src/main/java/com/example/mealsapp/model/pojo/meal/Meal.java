package com.example.mealsapp.model.pojo.meal;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Meal implements Parcelable {

    @PrimaryKey
    @NonNull
    private String idMeal;

    private String strArea;

    private String strCategory;

    private String strTags;

    private String strMealThumb;

    private String strYoutube;

    private String strMeal;

    private String strSource;

    private boolean isFavorite = false;

    private String strInstructions;

    private String strIngredient1;

    private String strIngredient2;

    private String strIngredient3;

    private String strIngredient4;

    private String strIngredient5;

    private String strIngredient6;

    private String strIngredient7;

    private String strIngredient8;

    private String strIngredient9;

    private String strIngredient10;

    private String strIngredient11;

    private String strIngredient12;

    private String strIngredient13;

    private String strIngredient14;

    private String strIngredient15;

    private String strIngredient17;

    private String strIngredient16;

    private String strIngredient18;

    private String strIngredient19;

    private String strIngredient20;

    public Meal(){}

    protected Meal(Parcel in) {
        idMeal = in.readString();
        strArea = in.readString();
        strCategory = in.readString();
        strTags = in.readString();
        strMealThumb = in.readString();
        strYoutube = in.readString();
        strMeal = in.readString();
        strSource = in.readString();
        isFavorite = in.readByte() != 0;
        strInstructions = in.readString();
        strIngredient1 = in.readString();
        strIngredient2 = in.readString();
        strIngredient3 = in.readString();
        strIngredient4 = in.readString();
        strIngredient5 = in.readString();
        strIngredient6 = in.readString();
        strIngredient7 = in.readString();
        strIngredient8 = in.readString();
        strIngredient9 = in.readString();
        strIngredient10 = in.readString();
        strIngredient11 = in.readString();
        strIngredient12 = in.readString();
        strIngredient13 = in.readString();
        strIngredient14 = in.readString();
        strIngredient15 = in.readString();
        strIngredient17 = in.readString();
        strIngredient16 = in.readString();
        strIngredient18 = in.readString();
        strIngredient19 = in.readString();
        strIngredient20 = in.readString();
    }

    public Meal(@NonNull String idMeal, String strArea,
                String strCategory, String strTags, String strMealThumb, String strYoutube, String strMeal,
                String strSource, boolean isFavorite, String strInstructions, String strIngredient1, String strIngredient2, String strIngredient3,
                String strIngredient4, String strIngredient5, String strIngredient6, String strIngredient7, String strIngredient8,
                String strIngredient9, String strIngredient10, String strIngredient11, String strIngredient12,
                String strIngredient13, String strIngredient14, String strIngredient15, String strIngredient17, String strIngredient16,
                String strIngredient18, String strIngredient19, String strIngredient20) {
        this.idMeal = idMeal;
        this.strArea = strArea;
        this.strCategory = strCategory;
        this.strTags = strTags;
        this.strMealThumb = strMealThumb;
        this.strYoutube = strYoutube;
        this.strMeal = strMeal;
        this.strSource = strSource;
        this.isFavorite = isFavorite;
        this.strInstructions = strInstructions;
        this.strIngredient1 = strIngredient1;
        this.strIngredient2 = strIngredient2;
        this.strIngredient3 = strIngredient3;
        this.strIngredient4 = strIngredient4;
        this.strIngredient5 = strIngredient5;
        this.strIngredient6 = strIngredient6;
        this.strIngredient7 = strIngredient7;
        this.strIngredient8 = strIngredient8;
        this.strIngredient9 = strIngredient9;
        this.strIngredient10 = strIngredient10;
        this.strIngredient11 = strIngredient11;
        this.strIngredient12 = strIngredient12;
        this.strIngredient13 = strIngredient13;
        this.strIngredient14 = strIngredient14;
        this.strIngredient15 = strIngredient15;
        this.strIngredient17 = strIngredient17;
        this.strIngredient16 = strIngredient16;
        this.strIngredient18 = strIngredient18;
        this.strIngredient19 = strIngredient19;
        this.strIngredient20 = strIngredient20;
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    public String getStrIngredient10() {
        return this.strIngredient10;
    }

    public void setStrIngredient10(String strIngredient10) {
        this.strIngredient10 = strIngredient10;
    }

    public String getStrIngredient12() {
        return this.strIngredient12;
    }

    public void setStrIngredient12(String strIngredient12) {
        this.strIngredient12 = strIngredient12;
    }

    public String getStrIngredient11() {
        return this.strIngredient11;
    }

    public void setStrIngredient11(String strIngredient11) {
        this.strIngredient11 = strIngredient11;
    }

    public String getStrIngredient14() {
        return this.strIngredient14;
    }

    public void setStrIngredient14(String strIngredient14) {
        this.strIngredient14 = strIngredient14;
    }

    public String getStrCategory() {
        return this.strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public String getStrIngredient13() {
        return this.strIngredient13;
    }

    public void setStrIngredient13(String strIngredient13) {
        this.strIngredient13 = strIngredient13;
    }

    public String getStrIngredient16() {
        return this.strIngredient16;
    }

    public void setStrIngredient16(String strIngredient16) {
        this.strIngredient16 = strIngredient16;
    }

    public String getStrIngredient15() {
        return this.strIngredient15;
    }

    public void setStrIngredient15(String strIngredient15) {
        this.strIngredient15 = strIngredient15;
    }

    public String getStrIngredient18() {
        return this.strIngredient18;
    }

    public void setStrIngredient18(String strIngredient18) {
        this.strIngredient18 = strIngredient18;
    }

    public String getStrIngredient17() {
        return this.strIngredient17;
    }

    public void setStrIngredient17(String strIngredient17) {
        this.strIngredient17 = strIngredient17;
    }

    public String getStrArea() {
        return this.strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }


    public String getStrIngredient19() {
        return this.strIngredient19;
    }

    public void setStrIngredient19(String strIngredient19) {
        this.strIngredient19 = strIngredient19;
    }

    public String getStrTags() {
        return this.strTags;
    }

    public void setStrTags(String strTags) {
        this.strTags = strTags;
    }

    public String getIdMeal() {
        return this.idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }

    public String getStrInstructions() {
        return this.strInstructions;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
    }

    public String getStrIngredient1() {
        return this.strIngredient1;
    }

    public void setStrIngredient1(String strIngredient1) {
        this.strIngredient1 = strIngredient1;
    }

    public String getStrIngredient3() {
        return this.strIngredient3;
    }

    public void setStrIngredient3(String strIngredient3) {
        this.strIngredient3 = strIngredient3;
    }

    public String getStrIngredient2() {
        return this.strIngredient2;
    }

    public void setStrIngredient2(String strIngredient2) {
        this.strIngredient2 = strIngredient2;
    }

    public String getStrIngredient20() {
        return this.strIngredient20;
    }

    public void setStrIngredient20(String strIngredient20) {
        this.strIngredient20 = strIngredient20;
    }

    public String getStrIngredient5() {
        return this.strIngredient5;
    }

    public void setStrIngredient5(String strIngredient5) {
        this.strIngredient5 = strIngredient5;
    }

    public String getStrIngredient4() {
        return this.strIngredient4;
    }

    public void setStrIngredient4(String strIngredient4) {
        this.strIngredient4 = strIngredient4;
    }

    public String getStrIngredient7() {
        return this.strIngredient7;
    }

    public void setStrIngredient7(String strIngredient7) {
        this.strIngredient7 = strIngredient7;
    }

    public String getStrIngredient6() {
        return this.strIngredient6;
    }

    public void setStrIngredient6(String strIngredient6) {
        this.strIngredient6 = strIngredient6;
    }

    public String getStrIngredient9() {
        return this.strIngredient9;
    }

    public void setStrIngredient9(String strIngredient9) {
        this.strIngredient9 = strIngredient9;
    }

    public String getStrIngredient8() {
        return this.strIngredient8;
    }

    public void setStrIngredient8(String strIngredient8) {
        this.strIngredient8 = strIngredient8;
    }

    public String getStrMealThumb() {
        return this.strMealThumb;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    public String getStrYoutube() {
        return this.strYoutube;
    }

    public void setStrYoutube(String strYoutube) {
        this.strYoutube = strYoutube;
    }

    public String getStrMeal() {
        return this.strMeal;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public String getStrSource() {
        return this.strSource;
    }

    public void setStrSource(String strSource) {
        this.strSource = strSource;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idMeal);
        dest.writeString(strArea);
        dest.writeString(strCategory);
        dest.writeString(strTags);
        dest.writeString(strMealThumb);
        dest.writeString(strYoutube);
        dest.writeString(strMeal);
        dest.writeString(strSource);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeString(strInstructions);
        dest.writeString(strIngredient1);
        dest.writeString(strIngredient2);
        dest.writeString(strIngredient3);
        dest.writeString(strIngredient4);
        dest.writeString(strIngredient5);
        dest.writeString(strIngredient6);
        dest.writeString(strIngredient7);
        dest.writeString(strIngredient8);
        dest.writeString(strIngredient9);
        dest.writeString(strIngredient10);
        dest.writeString(strIngredient11);
        dest.writeString(strIngredient12);
        dest.writeString(strIngredient13);
        dest.writeString(strIngredient14);
        dest.writeString(strIngredient15);
        dest.writeString(strIngredient17);
        dest.writeString(strIngredient16);
        dest.writeString(strIngredient18);
        dest.writeString(strIngredient19);
        dest.writeString(strIngredient20);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return isFavorite == meal.isFavorite && idMeal.equals(meal.idMeal) && Objects.equals(strArea, meal.strArea) && Objects.equals(strCategory, meal.strCategory) && Objects.equals(strTags, meal.strTags) && Objects.equals(strMealThumb, meal.strMealThumb) && Objects.equals(strYoutube, meal.strYoutube) && Objects.equals(strMeal, meal.strMeal) && Objects.equals(strSource, meal.strSource) && Objects.equals(strInstructions, meal.strInstructions) && Objects.equals(strIngredient1, meal.strIngredient1) && Objects.equals(strIngredient2, meal.strIngredient2) && Objects.equals(strIngredient3, meal.strIngredient3) && Objects.equals(strIngredient4, meal.strIngredient4) && Objects.equals(strIngredient5, meal.strIngredient5) && Objects.equals(strIngredient6, meal.strIngredient6) && Objects.equals(strIngredient7, meal.strIngredient7) && Objects.equals(strIngredient8, meal.strIngredient8) && Objects.equals(strIngredient9, meal.strIngredient9) && Objects.equals(strIngredient10, meal.strIngredient10) && Objects.equals(strIngredient11, meal.strIngredient11) && Objects.equals(strIngredient12, meal.strIngredient12) && Objects.equals(strIngredient13, meal.strIngredient13) && Objects.equals(strIngredient14, meal.strIngredient14) && Objects.equals(strIngredient15, meal.strIngredient15) && Objects.equals(strIngredient17, meal.strIngredient17) && Objects.equals(strIngredient16, meal.strIngredient16) && Objects.equals(strIngredient18, meal.strIngredient18) && Objects.equals(strIngredient19, meal.strIngredient19) && Objects.equals(strIngredient20, meal.strIngredient20);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMeal, strArea, strCategory, strTags, strMealThumb, strYoutube, strMeal, strSource, isFavorite, strInstructions, strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5, strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10, strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15, strIngredient17, strIngredient16, strIngredient18, strIngredient19, strIngredient20);
    }

    @Override
    public String toString() {
        return "Meal{" +
                "idMeal='" + idMeal + '\'' +
                ", strArea='" + strArea + '\'' +
                ", strCategory='" + strCategory + '\'' +
                ", strTags='" + strTags + '\'' +
                ", strMealThumb='" + strMealThumb + '\'' +
                ", strYoutube='" + strYoutube + '\'' +
                ", strMeal='" + strMeal + '\'' +
                ", strSource='" + strSource + '\'' +
                ", isFavorite=" + isFavorite +
                ", strInstructions='" + strInstructions + '\'' +
                ", strIngredient1='" + strIngredient1 + '\'' +
                ", strIngredient2='" + strIngredient2 + '\'' +
                ", strIngredient3='" + strIngredient3 + '\'' +
                ", strIngredient4='" + strIngredient4 + '\'' +
                ", strIngredient5='" + strIngredient5 + '\'' +
                ", strIngredient6='" + strIngredient6 + '\'' +
                ", strIngredient7='" + strIngredient7 + '\'' +
                ", strIngredient8='" + strIngredient8 + '\'' +
                ", strIngredient9='" + strIngredient9 + '\'' +
                ", strIngredient10='" + strIngredient10 + '\'' +
                ", strIngredient11='" + strIngredient11 + '\'' +
                ", strIngredient12='" + strIngredient12 + '\'' +
                ", strIngredient13='" + strIngredient13 + '\'' +
                ", strIngredient14='" + strIngredient14 + '\'' +
                ", strIngredient15='" + strIngredient15 + '\'' +
                ", strIngredient17='" + strIngredient17 + '\'' +
                ", strIngredient16='" + strIngredient16 + '\'' +
                ", strIngredient18='" + strIngredient18 + '\'' +
                ", strIngredient19='" + strIngredient19 + '\'' +
                ", strIngredient20='" + strIngredient20 + '\'' +
                '}';
    }
}
