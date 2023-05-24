package com.example.mealsapp.ui.home.favourites.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.FragmentFavouritesBinding;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.ui.home.favourites.presenter.FavouritesPresenter;
import com.example.mealsapp.ui.home.favourites.presenter.FavouritesPresenterImpl;
import com.example.mealsapp.ui.home.favourites.view.FavouritesFragmentDirections.ActionFavouritesFragmentToDetailsFragment;
import com.example.mealsapp.utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class FavouritesFragment extends Fragment implements FavouritesView {
    public static final String TAG = "FavouritesFragment";
    private FragmentFavouritesBinding binding;
    private FavouritesPresenter favouritesPresenter;
    private FavouritesAdapter adapter;
    private List<Meal> meals;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favouritesPresenter = new FavouritesPresenterImpl(this, ((MealsApplication) getActivity().getApplication()).getRepo());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BottomNavigationView navBar = getActivity().findViewById(R.id.navigation_bar);
        navBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Helper.showProgress(getContext());
        favouritesPresenter.getFavouriteMeals();
    }

    @Override
    public void showFavouriteMeals(List<Meal> meals) {
        Helper.hideProgress(getContext());
        this.meals = meals;
        Log.i(TAG, "showFavouriteMeals: " + meals.size());
        binding.rvFavMeals.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.rvFavMeals.setLayoutManager(layoutManager);
        adapter = new FavouritesAdapter(getContext(), new FavouritesAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(Meal meal) {
                Log.i("onBindViewHolder", "onClick: show Favourite details");
                ActionFavouritesFragmentToDetailsFragment action =
                        FavouritesFragmentDirections.actionFavouritesFragmentToDetailsFragment(meal);
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }

            @Override
            public void OnFavouriteClick(Meal meal) {
                Log.i("onBindViewHolder", "onClick: remove from favorite");
                favouritesPresenter.deleteMeal(meal);
            }
        });
        adapter.submitList(meals);
        binding.rvFavMeals.setAdapter(adapter);
    }

    @Override
    public void showError(Throwable throwable) {
        Helper.hideProgress(getContext());
        Toast.makeText(getContext(),
                "Error getting favourite meals",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mealDeletedSuccessfully(Meal meal) {
        int pos = meals.indexOf(meal);
        meals.remove(meal);
        Log.i(TAG, "showFavouriteMeals: meal deleted " + meals.size());
        adapter.notifyItemRemoved(pos);
        Toast.makeText(getContext(),
                "meal removed successfully",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mealDeletedFailure() {
        Toast.makeText(getContext(),
                "Error removing meal from list!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        favouritesPresenter.onDestroy();
    }
}