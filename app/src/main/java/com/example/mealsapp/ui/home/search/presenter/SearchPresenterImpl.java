package com.example.mealsapp.ui.home.search.presenter;

import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.model.pojo.category.Category;
import com.example.mealsapp.model.pojo.category.CategoryResponse;
import com.example.mealsapp.model.pojo.country.Country;
import com.example.mealsapp.model.pojo.country.CountryResponse;
import com.example.mealsapp.model.pojo.ingredient.Ingredient;
import com.example.mealsapp.model.pojo.ingredient.IngredientResponse;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.MealResponse;
import com.example.mealsapp.ui.home.search.view.SearchView;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenterImpl implements SearchPresenter {
    private SearchView view;
    private Repo repo;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<Category> categoryList;
    private List<Country> countryList;
    private List<Ingredient> ingredientList;

    public SearchPresenterImpl(SearchView view, Repo repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void searchByName(String name) {
        if (name.length() != 0) {
            Single<MealResponse> apiDataSingle = repo.searchByName(name).subscribeOn(Schedulers.io());
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
                    view.showSearchResultSuccess(meals);
                }

                @Override
                public void onError(Throwable e) {
                    view.showSearchResultError(e);
                }
            });
        } else {
            getCategories();
            getCountries();
            getIngredients();
        }
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
                        categoryList = categoryResponse.getMeals();
                        view.showCategories(categoryList);
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
                        countryList = countryResponse.getMeals();
                        view.showCountries(countryList);
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
                        ingredientList = ingredientResponse.getMeals();
                        view.showIngredients(ingredientList);
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
