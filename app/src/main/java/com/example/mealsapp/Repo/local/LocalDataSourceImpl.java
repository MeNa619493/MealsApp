package com.example.mealsapp.Repo.local;

import android.content.Context;
import com.example.mealsapp.model.data.local.MealDao;
import com.example.mealsapp.model.data.local.RoomDatabase;
import com.example.mealsapp.model.pojo.meal.Meal;
import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Single;

public class LocalDataSourceImpl  implements LocalDataSource{
    private MealDao dao;
    private static LocalDataSourceImpl instance;

    private LocalDataSourceImpl(Context context) {
        RoomDatabase roomDatabase = RoomDatabase.getInstance(context.getApplicationContext());
        this.dao = roomDatabase.mealDao();
    }

    public static synchronized LocalDataSourceImpl getInstance(Context context) {
        if (instance == null){
            instance = new LocalDataSourceImpl(context);
        }
        return instance;
    }

    @Override
    public Completable insertMeal(Meal meal) {
        return dao.insertMeal(meal);
    }

    @Override
    public Single<List<Meal>> getMeals() {
        return dao.getMeals();
    }

    @Override
    public Completable deleteMeal(Meal meal) {
        return dao.deleteMeal(meal);
    }

    @Override
    public Completable deleteAllMeals() {
        return dao.deleteAllMeals();
    }
}
