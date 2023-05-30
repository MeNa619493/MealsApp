package com.example.mealsapp.model.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefImpl implements SharedPref{
    private static SharedPrefImpl instance;
    private final SharedPreferences shared;
    private static final String ID ="id";
    private static final String LOGIN ="login";

    private SharedPrefImpl(Context context) {
        shared = context.getSharedPreferences("meals", Context.MODE_MULTI_PROCESS);
    }

    public static synchronized SharedPrefImpl getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefImpl(context);
        }
        return instance;
    }

    public void putIsLoggedInFlag(boolean isLoggedIn){
        shared.edit().putBoolean(LOGIN ,isLoggedIn).apply();
    }

    public boolean getIsLoggedInFlag(){
        return shared.getBoolean(LOGIN, false);
    }

    public void putUserID(String id){
        shared.edit().putString(ID ,id).apply();
    }

    public String getUserID(){
        return shared.getString(ID, "Empty");
    }
}

