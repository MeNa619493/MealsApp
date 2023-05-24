package com.example.mealsapp.ui.home.search.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.FragmentSearchBinding;
import com.example.mealsapp.model.pojo.category.Category;
import com.example.mealsapp.model.pojo.category.CategoryResponse;
import com.example.mealsapp.model.pojo.country.Country;
import com.example.mealsapp.model.pojo.country.CountryResponse;
import com.example.mealsapp.model.pojo.ingredient.Ingredient;
import com.example.mealsapp.model.pojo.ingredient.IngredientResponse;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.MealResponse;
import com.example.mealsapp.ui.home.search.presenter.SearchPresenter;
import com.example.mealsapp.ui.home.search.presenter.SearchPresenterImpl;
import com.example.mealsapp.ui.home.search.view.SearchFragmentDirections.ActionSearchFragmentToSearchByTypeFragment;
import com.example.mealsapp.utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchFragment extends Fragment implements SearchView {
    public static final String TAG = "SearchFragment";
    private FragmentSearchBinding binding;
    private SearchPresenter searchPresenter;

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

        observeViewAllText();
        Helper.showProgress(getContext());
        searchPresenter.getCategories();
        searchPresenter.getCountries();
        searchPresenter.getIngredients();
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
    public void showSearchResultSuccess(MealResponse mealResponse) {

    }

    @Override
    public void showCategories(CategoryResponse categoryResponse) {
        Log.i(TAG, "showCategories: " + categoryResponse.getMeals().size());
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
        adapter.submitList(categoryResponse.getMeals());
        binding.rvCategories.setAdapter(adapter);
    }

    @Override
    public void showIngredients(IngredientResponse ingredientResponse) {
        Helper.hideProgress(getContext());
        Log.i(TAG, "showIngredients: " + ingredientResponse.getMeals().size());
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
        adapter.submitList(ingredientResponse.getMeals());
        binding.rvIngredients.setAdapter(adapter);
    }

    @Override
    public void showCountries(CountryResponse countryResponse) {
        Log.i(TAG, "showCountries: " + countryResponse.getMeals().size());
        binding.rvCountries.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvCountries.setLayoutManager(layoutManager);
        CountriesAdapter adapter = new CountriesAdapter(getContext(), new CountriesAdapter.OnCountryClickListener() {
            @Override
            public void onCountryClick(Country country) {
                Log.i(TAG, "showIngredients: item clicked");
                ActionSearchFragmentToSearchByTypeFragment action =
                        SearchFragmentDirections
                                .actionSearchFragmentToSearchByTypeFragment("a", country.getStrArea());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });
        adapter.submitList(countryResponse.getMeals());
        binding.rvCountries.setAdapter(adapter);
    }

    @Override
    public void showError(Throwable throwable) {
        Helper.hideProgress(getContext());

    }

    @Override
    public void mealAddedToFavoriteSuccessfully(Meal meal) {

    }

    @Override
    public void mealAddedToFavoriteFailure() {

    }

    @Override
    public void mealDeletedSuccessfully(Meal meal) {

    }

    @Override
    public void mealDeletedFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        searchPresenter.onDestroy();
    }
}