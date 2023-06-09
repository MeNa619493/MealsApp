package com.example.mealsapp.ui.home.home.presenter;

import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.MealResponse;
import com.example.mealsapp.ui.home.home.view.HomeView;

import java.util.List;
import java.util.Random;

import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenterImpl implements HomePresenter {
    private HomeView view;
    private Repo repo;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    Single<List<Meal>> roomDataSingle;

    public HomePresenterImpl(HomeView view, Repo repo) {
        this.view = view;
        this.repo = repo;
        roomDataSingle = repo.getFavouriteMeals().subscribeOn(Schedulers.io());
    }

    @Override
    public void getRandomMeal() {
        Single<MealResponse> apiDataSingle = repo.getRandomMeal().subscribeOn(Schedulers.io());

        Single<Meal> combinedDataSingle = Single.zip(apiDataSingle, roomDataSingle,
                (apiDataList, roomDataList) -> {
                    Meal apiMeal = apiDataList.getMeals().get(0);

                    // Compare and find similar objects between the two lists
                    for (Meal roomData : roomDataList) {
                        if (apiMeal.getIdMeal().equals(roomData.getIdMeal())) { // Replace with your similarity check logic
                            apiMeal.setFavorite(true); // Change the flag in the similar object
                        }
                    }

                    return apiMeal; // Return the modified API data list
                }
        );

        combinedDataSingle
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Meal>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Meal meal) {
                        view.showMeal(meal);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e);
                    }
                });
    }

    @Override
    public void getSuggestionMeals() {
        Random r = new Random();
        char c = (char) (r.nextInt(26) + 'A');

        Single<MealResponse> apiDataSingle = repo.getSuggestionMeals(c).subscribeOn(Schedulers.io());

        Single<List<Meal>> combinedDataSingle = Single.zip(apiDataSingle, roomDataSingle,
                (apiDataList, roomDataList) -> {
                    List<Meal> apiList = apiDataList.getMeals();

                    // Compare and find similar objects between the two lists
                    for (Meal apiData : apiList) {
                        for (Meal roomData : roomDataList) {
                            if (apiData.getIdMeal().equals(roomData.getIdMeal())) { // Replace with your similarity check logic
                                apiData.setFavorite(true); // Change the flag in the similar object
                            }
                        }
                    }

                    return apiList; // Return the modified API data list
                }
        );

        combinedDataSingle
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Meal>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Meal> meals) {
                        view.showSuggestionMeals(meals);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e);
                    }
                });
    }

    @Override
    public void addFavorite(Meal meal) {
        repo.addFavorite(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        view.mealAddedToFavoriteSuccessfully(meal);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.mealAddedToFavoriteFailure();
                    }
                });
    }

    @Override
    public void deleteMeal(Meal meal) {
        repo.deleteFavorite(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        view.mealDeletedSuccessfully(meal);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.mealDeletedFailure();
                    }
                });
    }

    public boolean getIsLoggedInFlag() {
        return repo.getIsLoggedInFlag();
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}