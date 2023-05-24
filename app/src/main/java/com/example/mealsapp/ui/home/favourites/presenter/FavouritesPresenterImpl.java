package com.example.mealsapp.ui.home.favourites.presenter;

import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.ui.home.favourites.view.FavouritesView;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FavouritesPresenterImpl implements FavouritesPresenter{
    private FavouritesView view;
    private Repo repo;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public FavouritesPresenterImpl(FavouritesView view, Repo repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void getFavouriteMeals() {
        repo.getFavouriteMeals().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Meal>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Meal> meals) {
                        view.showFavouriteMeals(meals);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e);
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
        if(!compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }
}
