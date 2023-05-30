package com.example.mealsapp.ui.auth.register.view;

import com.example.mealsapp.model.pojo.user.User;

public interface RegisterView {
    void navigateToHome(User user);
    void showError(String message);
}
