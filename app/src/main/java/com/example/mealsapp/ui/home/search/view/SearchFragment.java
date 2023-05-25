package com.example.mealsapp.ui.home.search.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.FragmentSearchBinding;
import com.example.mealsapp.model.pojo.category.Category;
import com.example.mealsapp.model.pojo.country.Country;
import com.example.mealsapp.model.pojo.ingredient.Ingredient;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.ui.home.search.presenter.SearchPresenter;
import com.example.mealsapp.ui.home.search.presenter.SearchPresenterImpl;
import com.example.mealsapp.ui.home.search.view.SearchFragmentDirections.ActionSearchFragmentToSearchByTypeFragment;
import com.example.mealsapp.ui.home.search.view.SearchFragmentDirections.ActionSearchFragmentToDetailsFragment;
import com.example.mealsapp.utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class SearchFragment extends Fragment implements SearchView {
    public static final String TAG = "SearchFragment";
    private FragmentSearchBinding binding;
    private SearchPresenter searchPresenter;
    private SearchMealsAdapter adapter;
    private List<Meal> meals;
    private Disposable disposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchPresenter = new SearchPresenterImpl(this, ((MealsApplication) getActivity().getApplication()).getRepo());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BottomNavigationView navBar = getActivity().findViewById(R.id.navigation_bar);
        navBar.setVisibility(View.VISIBLE);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SpannableString content = new SpannableString("View All");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        binding.tvView.setText(content);

        Helper.showProgress(getContext());
        searchPresenter.getCategories();
        searchPresenter.getCountries();
        searchPresenter.getIngredients();
        observeViewAllText();
        observeSearchView();
    }

    private void observeSearchView() {
        disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                binding.etSearch.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        emitter.onNext(newText);
                        return false;
                    }
                });
            }
        }).subscribe(o -> searchPresenter.searchByName(o.toString()));
    }

    private void observeViewAllText() {
        binding.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(
                        SearchFragmentDirections.actionSearchFragmentToSearchByIngredientsFragment());
            }
        });
    }

    @Override
    public void showSearchResultSuccess(List<Meal> meals) {
        binding.group.setVisibility(View.GONE);
        binding.rvMeals.setVisibility(View.VISIBLE);
        Log.i(TAG, "showSearchResultSuccess: " + meals.size());
        this.meals = meals;
        ViewCompat.setNestedScrollingEnabled(binding.rvMeals, false);
        binding.rvMeals.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.rvMeals.setLayoutManager(layoutManager);
        SimpleItemAnimator itemAnimator = (SimpleItemAnimator) binding.rvMeals.getItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        adapter = new SearchMealsAdapter(getContext(), new SearchMealsAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(Meal meal) {
                Log.i(TAG, "onClick: show details");
                ActionSearchFragmentToDetailsFragment action =
                        SearchFragmentDirections.actionSearchFragmentToDetailsFragment(meal);
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }

            @Override
            public void OnFavouriteClick(Meal meal) {
                if (meal.isFavorite()) {
                    meal.setFavorite(false);
                    Log.i(TAG, "onClick: remove from favorite");
                    searchPresenter.deleteMeal(meal);
                } else {
                    meal.setFavorite(true);
                    Log.i(TAG, "onClick: add to favorite");
                    searchPresenter.addFavorite(meal);
                }
            }
        });
        adapter.submitList(meals);
        binding.rvMeals.setAdapter(adapter);
    }

    @Override
    public void showCategories(List<Category> categories) {
        Log.i(TAG, "showCategories: " + categories.size());
        binding.rvCategories.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.rvCategories.setLayoutManager(layoutManager);
        CategoriesAdapter adapter = new CategoriesAdapter(getContext(), new CategoriesAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(Category category) {
                Log.i(TAG, "showCategories: item clicked");
                ActionSearchFragmentToSearchByTypeFragment action =
                        SearchFragmentDirections
                                .actionSearchFragmentToSearchByTypeFragment("c", category.getStrCategory());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });
        adapter.submitList(categories);
        binding.rvCategories.setAdapter(adapter);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        Helper.hideProgress(getContext());
        binding.group.setVisibility(View.VISIBLE);
        binding.rvMeals.setVisibility(View.GONE);
        Log.i(TAG, "showIngredients: " + ingredients.size());
        binding.rvIngredients.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvIngredients.setLayoutManager(layoutManager);
        IngredientsAdapter adapter = new IngredientsAdapter(new IngredientsAdapter.OnIngredientClickListener() {
            @Override
            public void onIngredientClick(Ingredient ingredient) {
                Log.i(TAG, "showIngredients: item clicked");
                ActionSearchFragmentToSearchByTypeFragment action =
                        SearchFragmentDirections
                                .actionSearchFragmentToSearchByTypeFragment("i", ingredient.getStrIngredient());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });
        adapter.submitList(ingredients);
        binding.rvIngredients.setAdapter(adapter);
    }

    @Override
    public void showCountries(List<Country> countries) {
        Log.i(TAG, "showCountries: " + countries.size());
        binding.rvCountries.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvCountries.setLayoutManager(layoutManager);
        CountriesAdapter adapter = new CountriesAdapter(getContext(), new CountriesAdapter.OnCountryClickListener() {
            @Override
            public void onCountryClick(Country country) {
                Log.i(TAG, "showCountries: item clicked");
                ActionSearchFragmentToSearchByTypeFragment action =
                        SearchFragmentDirections
                                .actionSearchFragmentToSearchByTypeFragment("a", country.getStrArea());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });
        adapter.submitList(countries);
        binding.rvCountries.setAdapter(adapter);
    }

    @Override
    public void showError(Throwable throwable) {
        Helper.hideProgress(getContext());
        Toast.makeText(binding.getRoot().getContext(),
                "Error getting meals",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mealAddedToFavoriteSuccessfully(Meal meal) {
        int pos = meals.indexOf(meal);
        adapter.notifyItemChanged(pos);
        Toast.makeText(binding.getRoot().getContext(),
                "Meal Saved Successfully",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mealAddedToFavoriteFailure() {
        Toast.makeText(binding.getRoot().getContext(),
                "Error, please try again later!!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mealDeletedSuccessfully(Meal meal) {
        int pos = meals.indexOf(meal);
        adapter.notifyItemChanged(pos);
        Toast.makeText(getContext(),
                "meal removed successfully",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mealDeletedFailure() {
        Toast.makeText(getContext(),
                "Error,didn't removing meal from list!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
        binding = null;
        searchPresenter.onDestroy();
    }
}