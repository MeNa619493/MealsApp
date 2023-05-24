package com.example.mealsapp;

import android.app.Application;
import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.Repo.local.LocalDataSource;
import com.example.mealsapp.Repo.local.LocalDataSourceImpl;
import com.example.mealsapp.Repo.remote.ApiClient;

public class MealsApplication extends Application {
    private Repo repo;

    @Override
    public void onCreate() {
        super.onCreate();

        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance(getApplicationContext());
        ApiClient apiClient = ApiClient.getInstance();
        repo = new Repo(localDataSource, apiClient);
    }

    public Repo getRepo() {
        return repo;
    }
}
