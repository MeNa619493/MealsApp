package com.example.mealsapp.ui.home.search.presenter;

import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.model.pojo.category.CategoryResponse;
import com.example.mealsapp.model.pojo.country.CountryResponse;
import com.example.mealsapp.model.pojo.ingredient.IngredientResponse;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.MealResponse;
import com.example.mealsapp.ui.home.home.view.HomeView;
import com.example.mealsapp.ui.home.search.view.SearchView;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenterImpl implements SearchPresenter{
    private SearchView view;
    private Repo repo;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SearchPresenterImpl(SearchView view, Repo repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void searchByName(String name) {

    }

    @Override
    public void getCategories() {
        repo.getCategories().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CategoryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(CategoryResponse categoryResponse) {
                        view.showCategories(categoryResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e);
                    }
                });
    }

    @Override
    public void getCountries() {
        repo.getCountries().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CountryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(CountryResponse countryResponse) {
                        view.showCountries(countryResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e);
                    }
                });
    }

    @Override
    public void getIngredients() {
        repo.getIngredients().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<IngredientResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(IngredientResponse ingredientResponse) {
                        view.showIngredients(ingredientResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e);
                    }
                });
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
