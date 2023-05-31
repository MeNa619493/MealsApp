package com.example.mealsapp.ui.home.home.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.FragmentHomeBinding;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.MealResponse;
import com.example.mealsapp.ui.auth.MainActivity;
import com.example.mealsapp.ui.home.HomeActivity;
import com.example.mealsapp.ui.home.home.presenter.HomePresenter;
import com.example.mealsapp.ui.home.home.presenter.HomePresenterImpl;
import java.util.List;
import com.example.mealsapp.ui.home.home.view.HomeFragmentDirections.ActionHomeFragmentToDetailsFragment;
import com.example.mealsapp.utils.NetworkChangeReceiver;
import com.example.mealsapp.utils.ProgressDialogHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment implements HomeView{
    public static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private HomePresenter homePresenter;
    private MealsAdapter adapter;
    private List<Meal> meals;
    private boolean isActionBarVisible = true;
    private Toolbar actionBar;

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

        actionBar = getActivity().findViewById(R.id.toolbar);
        actionBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerNetworkStateReceiver();
        hideToolbarWhenScrolling();

        ProgressDialogHelper.showProgress(getContext());
        NetworkChangeReceiver.getIsConnected().observe(getViewLifecycleOwner(), isConnected -> {
            handleNoConnection(isConnected);
        });

    }

    private void handleNoConnection(boolean isVisible) {
        if (isVisible) {
            binding.screenLayout.setVisibility(View.VISIBLE);
            binding.ivNoInternet.setVisibility(View.GONE);
            homePresenter.getRandomMeal();
            homePresenter.getSuggestionMeals();
        } else {
            ProgressDialogHelper.hideProgress(getContext());
            binding.screenLayout.setVisibility(View.GONE);
            binding.ivNoInternet.setVisibility(View.VISIBLE);
        }
    }

    private void registerNetworkStateReceiver() {
        NetworkChangeReceiver networkStateReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        requireContext().registerReceiver(networkStateReceiver, intentFilter);
    }

    private void hideToolbarWhenScrolling() {
        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = binding.scrollView.getScrollY();

                if (scrollY > 0 && isActionBarVisible) {
                    // Hide the action bar
                    if (actionBar != null) {
                        actionBar.setVisibility(View.GONE);
                    }
                    isActionBarVisible = false;
                } else if (scrollY == 0 && !isActionBarVisible) {
                    // Show the action bar
                    if (actionBar != null) {
                        actionBar.setVisibility(View.VISIBLE);
                    }
                    isActionBarVisible = true;
                }
            }
        });
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
        observeSingleCardAction(meal);
    }

    private void observeSingleCardAction(Meal meal) {
       binding.ivSaveMeal.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (homePresenter.getIsLoggedInFlag()) {
                   if (meal.isFavorite()) {
                       meal.setFavorite(false);
                       Log.i(TAG, "onClick: remove single meal from favorite");
                       homePresenter.deleteMeal(meal);
                       binding.ivSaveMeal.setImageDrawable(getResources().getDrawable(R.drawable.bookmark));
                   } else {
                       meal.setFavorite(true);
                       Log.i(TAG, "onClick: add single meal to favorite");
                       homePresenter.addFavorite(meal);
                       binding.ivSaveMeal.setImageDrawable(getResources().getDrawable(R.drawable.bookmark_filled));
                   }
               } else {
                   showAlertDialog();
               }
           }
       });
        binding.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: show single meal details");
                ActionHomeFragmentToDetailsFragment action =
                        HomeFragmentDirections.actionHomeFragmentToDetailsFragment(meal);
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });
    }

    @Override
    public void showSuggestionMeals(List<Meal> meals) {
        ProgressDialogHelper.hideProgress(getContext());
        Log.i(TAG, "showSuggestionMeals: " + meals.size());
        this.meals = meals;
        binding.rvMeals.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
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
               if (homePresenter.getIsLoggedInFlag()) {
                   if (meal.isFavorite()) {
                       meal.setFavorite(false);
                       Log.i(TAG , "onClick: remove from favorite");
                       homePresenter.deleteMeal(meal);
                   } else {
                       meal.setFavorite(true);
                       Log.i(TAG , "onClick: add to favorite");
                       homePresenter.addFavorite(meal);
                   }
               } else {
                   showAlertDialog();
               }
            }
        });
        adapter.submitList(meals);
        binding.rvMeals.setAdapter(adapter);
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(requireContext()).setTitle("Sign In Required")
                .setMessage("You must sign in to access this feature.")
                .setIcon(R.drawable.login)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create()
                .show();
    }

    @Override
    public void showError(Throwable throwable) {
        ProgressDialogHelper.hideProgress(getContext());
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



