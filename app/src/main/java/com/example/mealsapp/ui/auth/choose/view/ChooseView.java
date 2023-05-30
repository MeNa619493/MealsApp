package com.example.mealsapp.ui.auth.choose.view;

import com.example.mealsapp.model.pojo.user.User;

public interface ChooseView {
    void navigateToHome(User user);
    void showError(String message);
}
