package com.example.mealsapp.ui.home.home.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.FragmentHomeBinding;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.MealResponse;
import com.example.mealsapp.ui.home.home.presenter.HomePresenter;
import com.example.mealsapp.ui.home.home.presenter.HomePresenterImpl;
import java.util.List;
import com.example.mealsapp.ui.home.home.view.HomeFragmentDirections.ActionHomeFragmentToDetailsFragment;
import com.example.mealsapp.utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment implements HomeView{
    public static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private HomePresenter homePresenter;
    private MealsAdapter adapter;
    private List<Meal> meals;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homePresenter = new HomePresenterImpl(this, ((MealsApplication)getActivity().getApplication()).getRepo());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
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
        homePresenter.getRandomMeal();
        homePresenter.getSuggestionMeals();
    }

    @Override
    public void showMeal(MealResponse mealResponse) {
        Meal meal = mealResponse.getMeals().get(0);
        binding.tvMealName.setText(meal.getStrMeal());
        binding.tvCategory.setText(meal.getStrCategory());
        Glide.with(binding.getRoot())
                .load(meal.getStrMealThumb())
                .placeholder(R.drawable.placeholder)
                .into(binding.ivMeal);
    }

    @Override
    public void showSuggestionMeals(List<Meal> meals) {
        Helper.hideProgress(getContext());
        Log.i(TAG, "showSuggestionMeals: " + meals.size());
        this.meals = meals;
        binding.rvMeals.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvMeals.setLayoutManager(layoutManager);
        SimpleItemAnimator itemAnimator = (SimpleItemAnimator) binding.rvMeals.getItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        adapter = new MealsAdapter(getContext(), new MealsAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(Meal meal) {
                Log.i(TAG, "onClick: show details");
                ActionHomeFragmentToDetailsFragment action =
                        HomeFragmentDirections.actionHomeFragmentToDetailsFragment(meal);
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }

            @Override
            public void OnFavouriteClick(Meal meal) {
                if (meal.isFavorite()) {
                    meal.setFavorite(false);
                    Log.i(TAG , "onClick: remove from favorite");
                    homePresenter.deleteMeal(meal);
                } else {
                    meal.setFavorite(true);
                    Log.i(TAG , "onClick: add to favorite");
                    homePresenter.addFavorite(meal);
                }
            }
        });
        adapter.submitList(meals);
        binding.rvMeals.setAdapter(adapter);
    }

    @Override
    public void showError(Throwable throwable) {
        Helper.hideProgress(getContext());
        Toast.makeText(binding.getRoot().getContext(),
                "Error getting random meal",
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
        binding = null;
        homePresenter.onDestroy();
    }
}
