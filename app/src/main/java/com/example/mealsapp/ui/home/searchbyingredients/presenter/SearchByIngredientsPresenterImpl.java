package com.example.mealsapp.ui.home.searchbyingredients.presenter;

import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.model.pojo.ingredient.Ingredient;
import com.example.mealsapp.model.pojo.ingredient.IngredientResponse;
import com.example.mealsapp.ui.home.searchbyingredients.view.SearchByIngredientsView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchByIngredientsPresenterImpl implements SearchByIngredientsPresenter {
    private SearchByIngredientsView view;
    private Repo repo;
    private List<Ingredient> ingredients;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SearchByIngredientsPresenterImpl(SearchByIngredientsView view, Repo repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void searchIngredientsByName(String name) {
        if (name.length() != 0) {
            Observable.fromIterable(ingredients)
                    .filter(ingredient -> ingredient.getStrIngredient().toLowerCase().contains(name.toLowerCase()))
                    .toList()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<Ingredient>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onSuccess(List<Ingredient> ingredients) {
                            view.showIngredientsSearchResult(ingredients);
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showError(e);
                        }
                    });
        } else {
            view.showIngredientsSearchResult(ingredients);
        }
    }

    @Override
    public void getIngredients() {
        repo.getIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<IngredientResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(IngredientResponse ingredientResponse) {
                        ingredients = ingredientResponse.getMeals();
                        view.showIngredients(ingredientResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e);
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
