package com.example.mealsapp.ui.home.searchbytype.presenter;

import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.MealResponse;
import com.example.mealsapp.ui.home.searchbytype.view.SearchByTypeView;
import java.util.List;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchByTypePresenterImpl implements SearchByTypePresenter {
    private SearchByTypeView view;
    private Repo repo;
    private List<Meal> mealList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SearchByTypePresenterImpl(SearchByTypeView view, Repo repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void getMealsByCategory(String categoryName) {
        Single<MealResponse> apiDataSingle = repo.searchByCategory(categoryName).subscribeOn(Schedulers.io());
        getMeals(apiDataSingle);
    }

    @Override
    public void getMealsByCountry(String countryName) {
        Single<MealResponse> apiDataSingle = repo.searchByCountry(countryName).subscribeOn(Schedulers.io());
        getMeals(apiDataSingle);
    }

    @Override
    public void getMealsByIngredient(String ingredientName) {
        Single<MealResponse> apiDataSingle = repo.searchByIngredient(ingredientName).subscribeOn(Schedulers.io());
        getMeals(apiDataSingle);
    }

    private void getMeals(Single<MealResponse> apiDataSingle) {
        Single<List<Meal>> roomDataSingle = repo.getFavouriteMeals().subscribeOn(Schedulers.io());

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
        ).subscribeOn(Schedulers.io());

        combinedDataSingle.observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Meal>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<Meal> meals) {
                mealList = meals;
                view.showMeals(meals);
            }

            @Override
            public void onError(Throwable e) {
                view.showError(e);
            }
        });
    }

    @Override
    public void searchMealByName(String name) {
        if (name.length() != 0){
            Observable.fromIterable(mealList)
                    .filter(meal -> meal.getStrMeal().toLowerCase().contains(name.toLowerCase()))
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<Meal>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onSuccess(List<Meal> meals) {
                            view.showMeals(meals);
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showError(e);
                        }
                    });
        } else {
            view.showMeals(mealList);
        }
    }

    @Override
    public void addFavorite(Meal meal) {
        repo.addFavorite(meal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
        repo.deleteFavorite(meal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
