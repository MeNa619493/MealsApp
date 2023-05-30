package com.example.mealsapp.ui.auth.login.view;

import com.example.mealsapp.model.pojo.user.User;

public interface LoginView {
    void navigateToHome(User user);
    void showError(String message);
}
