package com.example.mealsapp.model.pojo.user;

import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.PlannedMeal;

import java.util.List;

public class User {
    private String id;
    private String userName;
    private String email;
    private String passWord;
    private List<Meal> favorites;
    private List<PlannedMeal> planned;

    public User(){}

    public User(String id, String userName, String email, String passWord, List<Meal> favorites, List<PlannedMeal> planned) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.passWord = passWord;
        this.favorites = favorites;
        this.planned = planned;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public List<Meal> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Meal> favorites) {
        this.favorites = favorites;
    }

    public List<PlannedMeal> getPlanned() {
        return planned;
    }

    public void setPlanned(List<PlannedMeal> planned) {
        this.planned = planned;
    }
}
