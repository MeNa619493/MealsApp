package com.example.mealsapp.model.data.local;

public interface SharedPref {

    void putIsLoggedInFlag(boolean isLoggedIn);

    boolean getIsLoggedInFlag();

    void putUserID(String id);

    String getUserID();
}
