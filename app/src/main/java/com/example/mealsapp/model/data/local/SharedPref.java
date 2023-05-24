package com.example.mealsapp.model.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static SharedPref instance;
    private final SharedPreferences shared;

    private SharedPref(Context context) {
        shared = context.getSharedPreferences("meals", Context.MODE_MULTI_PROCESS);
    }

    public static synchronized SharedPref getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPref(context);
        }
        return instance;
    }
}

