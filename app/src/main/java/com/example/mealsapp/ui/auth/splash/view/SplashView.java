package com.example.mealsapp.ui.auth.splash.view;

import com.example.mealsapp.model.pojo.user.User;

public interface SplashView {
    void navigateToHome(User user);
    void showError(String message);
}
