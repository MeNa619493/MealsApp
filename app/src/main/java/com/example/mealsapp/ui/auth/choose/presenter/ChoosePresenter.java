package com.example.mealsapp.ui.auth.choose.presenter;

import com.example.mealsapp.model.pojo.user.User;

public interface ChoosePresenter {
    void checkIfUserExist(User user);
    void putIsLoggedInFlag(boolean isLoggedIn);
    void onDestroy();
}
