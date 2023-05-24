package com.example.mealsapp.ui.home.details.presnter;

import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.ui.home.details.view.DetailsView;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsPresenterImpl implements DetailsPresenter{
    private DetailsView view;
    private Repo repo;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DetailsPresenterImpl(DetailsView view, Repo repo) {
        this.view = view;
        this.repo = repo;
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
        if(!compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }
}
