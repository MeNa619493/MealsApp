package com.example.mealsapp.ui.auth.login.presenter;

import com.example.mealsapp.model.pojo.user.User;

public interface LoginPresenter {
    void validateUserInput(User user);
    void onDestroy();
}
