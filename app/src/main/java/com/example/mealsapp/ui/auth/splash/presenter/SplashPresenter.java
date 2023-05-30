package com.example.mealsapp.ui.auth.splash.presenter;


public interface SplashPresenter {
    boolean getIsLoggedInFlag();
    String getUserID();
    void getUserDetails(String id);
    void onDestroy();
}
