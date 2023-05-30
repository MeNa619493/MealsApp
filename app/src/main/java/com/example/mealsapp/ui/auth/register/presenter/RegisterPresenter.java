package com.example.mealsapp.ui.auth.register.presenter;

import android.app.Activity;

import com.example.mealsapp.model.pojo.user.User;

public interface RegisterPresenter {
    void validateUserInput(User user);
    void onDestroy();
}
