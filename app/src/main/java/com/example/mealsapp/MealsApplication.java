package com.example.mealsapp;

import android.app.Application;

import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.Repo.local.LocalDataSource;
import com.example.mealsapp.Repo.local.LocalDataSourceImpl;
import com.example.mealsapp.Repo.remote.ApiClient;
import com.example.mealsapp.model.data.local.SharedPref;
import com.example.mealsapp.model.data.local.SharedPrefImpl;

public class MealsApplication extends Application {
    private Repo repo;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPref sharedPref = SharedPrefImpl.getInstance(getApplicationContext());
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance(getApplicationContext());
        ApiClient apiClient = ApiClient.getInstance();
        repo = new Repo(localDataSource, apiClient, sharedPref);
    }

    public Repo getRepo() {
        return repo;
    }
}
