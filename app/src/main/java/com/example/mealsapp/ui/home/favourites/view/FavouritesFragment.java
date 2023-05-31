package com.example.mealsapp.ui.home.favourites.view;

import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.FragmentFavouritesBinding;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.ui.home.favourites.presenter.FavouritesPresenter;
import com.example.mealsapp.ui.home.favourites.presenter.FavouritesPresenterImpl;
import com.example.mealsapp.ui.home.favourites.view.FavouritesFragmentDirections.ActionFavouritesFragmentToDetailsFragment;
import com.example.mealsapp.utils.NetworkChangeReceiver;
import com.example.mealsapp.utils.ProgressDialogHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FavouritesFragment extends Fragment implements FavouritesView {
    public static final String TAG = "FavouritesFragment";
    private FragmentFavouritesBinding binding;
    private FavouritesPresenter favouritesPresenter;
    private FavouritesAdapter adapter;
    private List<Meal> meals;
    private Snackbar snackbar;
    private BottomNavigationView navBar;
    private Toolbar actionBar;

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
        navBar = getActivity().findViewById(R.id.navigation_bar);
        navBar.setVisibility(View.VISIBLE);

        actionBar = getActivity().findViewById(R.id.toolbar);
        actionBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressDialogHelper.showProgress(getContext());
        favouritesPresenter.getFavouriteMeals();

        NetworkChangeReceiver.getIsConnected().observe(getViewLifecycleOwner(), isConnected -> {
            handleNoConnection(isConnected);
        });
    }

    private void handleNoConnection(boolean isVisible) {
        if (isVisible) {
            hideSnackbar();
        } else {
            showSnackbar();
        }
    }

    private void showSnackbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackbar = Snackbar.make(binding.getRoot(), "", Snackbar.LENGTH_INDEFINITE);
            Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorSnackBarError));
            TextView textView = new TextView(requireContext());
            textView.setText("No network connection");
            textView.setTextColor(requireContext().getColor(R.color.white));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            snackbarLayout.addView(textView, 0);
            snackbar.setAnchorView(navBar);
            snackbar.show();
        }
    }

    private void hideSnackbar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    @Override
    public void showFavouriteMeals(List<Meal> meals) {
        ProgressDialogHelper.hideProgress(getContext());
        if (meals.size() > 0) {
            this.meals = meals;
            Log.i(TAG, "showFavouriteMeals: " + meals.size());
            binding.rvFavMeals.setVisibility(View.VISIBLE);
            binding.ivNoDataFound.setVisibility(View.GONE);
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
        } else {
            showNoDataView();
        }
    }

    private void showNoDataView() {
        binding.rvFavMeals.setVisibility(View.GONE);
        binding.ivNoDataFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable throwable) {
        ProgressDialogHelper.hideProgress(getContext());
        Toast.makeText(getContext(),
                "Error getting favourite meals",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mealDeletedSuccessfully(Meal meal) {
        int pos = meals.indexOf(meal);
        meals.remove(meal);
        adapter.notifyItemRemoved(pos);
        Log.i(TAG, "showFavouriteMeals: meal deleted " + meals.size());
        if (meals.size() <= 0){
            showNoDataView();
        }
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
        hideSnackbar();
        binding = null;
        favouritesPresenter.onDestroy();
    }
}