package com.example.mealsapp.ui.home.calender.presenter;

import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.model.pojo.meal.PlannedMeal;
import com.example.mealsapp.ui.home.calender.view.CalendarView;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CalendarPresenterImpl implements CalendarPresenter {
    private CalendarView view;
    private Repo repo;
    private List<PlannedMeal> meals;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CalendarPresenterImpl(CalendarView view, Repo repo) {
        this.view = view;
        this.repo = repo;
    }

    public void getAllPlannedMeals(String date) {
        if (meals == null) {
            repo.getAllPlannedMeals()
                    .subscribeOn(Schedulers.io())
                    .subscribe(new SingleObserver<List<PlannedMeal>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onSuccess(List<PlannedMeal> plannedMeals) {
                            meals = plannedMeals;
                            getPlannedMeals(date);
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showError(e);
                        }
                    });
        } else {
            getPlannedMeals(date);
        }
    }

    private void getPlannedMeals(String date) {
        Observable.fromIterable(meals)
                .filter(meal -> meal.getDate().equalsIgnoreCase(date))
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<PlannedMeal>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<PlannedMeal> plannedMeals) {
                        view.showPlannedMeals(plannedMeals, date);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e);
                    }
                });
    }

    @Override
    public void deleteMeal(PlannedMeal meal) {
        repo.deletePlannedMeal(meal)
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

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
