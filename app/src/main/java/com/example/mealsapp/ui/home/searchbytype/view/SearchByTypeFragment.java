package com.example.mealsapp.ui.home.searchbytype.view;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.FragmentSearchByTypeBinding;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.ui.home.HomeActivity;
import com.example.mealsapp.ui.home.searchbytype.view.SearchByTypeFragmentDirections.ActionSearchByTypeFragmentToDetailsFragment;
import com.example.mealsapp.ui.home.searchbytype.presenter.SearchByTypePresenter;
import com.example.mealsapp.ui.home.searchbytype.presenter.SearchByTypePresenterImpl;
import com.example.mealsapp.utils.NetworkChangeReceiver;
import com.example.mealsapp.utils.ProgressDialogHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class SearchByTypeFragment extends Fragment implements SearchByTypeView {
    public static final String TAG = "SearchByTypeFragment";
    private FragmentSearchByTypeBinding binding;
    private SearchByTypePresenter searchByTypePresenter;
    private Disposable disposable;
    private SearchByTypeAdapter adapter;
    private List<Meal> meals;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchByTypePresenter = new SearchByTypePresenterImpl(this, ((MealsApplication) getActivity().getApplication()).getRepo());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchByTypeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BottomNavigationView navBar = getActivity().findViewById(R.id.navigation_bar);
        navBar.setVisibility(View.GONE);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observeCancelButton();
        observeSearchView();

        ProgressDialogHelper.showProgress(getContext());
        NetworkChangeReceiver.getIsConnected().observe(getViewLifecycleOwner(), isConnected -> {
            handleNoConnection(isConnected);
        });
    }

    private void handleNoConnection(boolean isVisible) {
        if (isVisible) {
            binding.rvMeals.setVisibility(View.VISIBLE);
            binding.ivNoInternet.setVisibility(View.GONE);
            getData();
        } else {
            binding.rvMeals.setVisibility(View.GONE);
            binding.ivNoInternet.setVisibility(View.VISIBLE);
        }
    }

    private void observeCancelButton() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        });
    }

    private void observeSearchView() {
        disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                binding.etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        }).subscribe(o -> searchByTypePresenter.searchMealByName(o.toString()));
    }

    private void getData() {
        String type = SearchByTypeFragmentArgs.fromBundle(getArguments()).getType();
        String searchField = SearchByTypeFragmentArgs.fromBundle(getArguments()).getSearchName();
        if (type.equalsIgnoreCase("c")) {
            searchByTypePresenter.getMealsByCategory(searchField);
        } else if (type.equalsIgnoreCase("a")) {
            searchByTypePresenter.getMealsByCountry(searchField);
        } else {
            searchByTypePresenter.getMealsByIngredient(searchField);
        }
    }

    @Override
    public void showMeals(List<Meal> meals) {
        ProgressDialogHelper.hideProgress(getContext());
        this.meals = meals;
        Log.i(TAG, "showMeals: " + meals.size());
        binding.rvMeals.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.rvMeals.setLayoutManager(layoutManager);
        SimpleItemAnimator itemAnimator = (SimpleItemAnimator) binding.rvMeals.getItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        adapter = new SearchByTypeAdapter(getContext(), new SearchByTypeAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(Meal meal) {
                Log.i(TAG, "onClick: show Favourite details");
                ActionSearchByTypeFragmentToDetailsFragment action =
                        SearchByTypeFragmentDirections.actionSearchByTypeFragmentToDetailsFragment(meal);
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }

            @Override
            public void OnFavouriteClick(Meal meal) {
                if (searchByTypePresenter.getIsLoggedInFlag()) {
                    if (meal.isFavorite()) {
                        meal.setFavorite(false);
                        Log.i(TAG, "onClick: remove from favorite");
                        searchByTypePresenter.deleteMeal(meal);
                    } else {
                        meal.setFavorite(true);
                        Log.i(TAG, "onClick: add to favorite");
                        searchByTypePresenter.addFavorite(meal);
                    }
                } else {
                    showAlertDialog();
                }
            }
        });
        adapter.submitList(meals);
        binding.rvMeals.setAdapter(adapter);
    }

    @Override
    public void showMealsSearchResult(List<Meal> meals) {
        adapter.submitList(meals);
    }

    @Override
    public void showError(Throwable throwable) {
        ProgressDialogHelper.hideProgress(getContext());
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

    private void showAlertDialog() {
        new AlertDialog.Builder(requireContext()).setTitle("Sign In Required")
                .setMessage("You must sign in to access this feature.")
                .setIcon(R.drawable.login)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(requireContext(), HomeActivity.class);
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
        searchByTypePresenter.onDestroy();
    }
}