package com.example.mealsapp.ui.home.searchbyingredients.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.FragmentSearchByIngredientsBinding;
import com.example.mealsapp.model.pojo.ingredient.Ingredient;
import com.example.mealsapp.model.pojo.ingredient.IngredientResponse;
import com.example.mealsapp.ui.home.searchbyingredients.presenter.SearchByIngredientsPresenter;
import com.example.mealsapp.ui.home.searchbyingredients.presenter.SearchByIngredientsPresenterImpl;
import com.example.mealsapp.ui.home.searchbyingredients.view.SearchByIngredientsFragmentDirections.ActionSearchByIngredientsFragmentToSearchByTypeFragment;
import com.example.mealsapp.utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class SearchByIngredientsFragment extends Fragment implements SearchByIngredientsView {
    public static final String TAG = "SearchByIngredients";
    private FragmentSearchByIngredientsBinding binding;
    private SearchByIngredientsPresenter searchByIngredientsPresenter;
    private Disposable disposable;
    private SearchByIngredientsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchByIngredientsPresenter =
                new SearchByIngredientsPresenterImpl(this, ((MealsApplication) getActivity().getApplication()).getRepo());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchByIngredientsBinding.inflate(inflater, container, false);
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

        Helper.showProgress(getContext());
        searchByIngredientsPresenter.getIngredients();
        observeSearchView();
        observeCancelButton();
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
        }).subscribe(o -> searchByIngredientsPresenter.searchIngredientsByName(o.toString()));
    }

    @Override
    public void showIngredients(IngredientResponse ingredientResponse) {
        Helper.hideProgress(getContext());
        Log.i(TAG, "showIngredients: " + ingredientResponse.getMeals().size());
        binding.rvIngredients.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        binding.rvIngredients.setLayoutManager(layoutManager);
        adapter = new SearchByIngredientsAdapter(new SearchByIngredientsAdapter.OnIngredientClickListener() {
            @Override
            public void onIngredientClick(Ingredient ingredient) {
                Log.i(TAG, "showIngredients: item clicked");
                ActionSearchByIngredientsFragmentToSearchByTypeFragment action =
                        SearchByIngredientsFragmentDirections
                                .actionSearchByIngredientsFragmentToSearchByTypeFragment("i", ingredient.getStrIngredient());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });
        adapter.submitList(ingredientResponse.getMeals());
        binding.rvIngredients.setAdapter(adapter);
    }

    @Override
    public void showIngredientsSearchResult(List<Ingredient> ingredients) {
        adapter.submitList(ingredients);
    }

    @Override
    public void showError(Throwable throwable) {
        Helper.hideProgress(getContext());
        Toast.makeText(binding.getRoot().getContext(),
                "Error getting search Result",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        binding = null;
        searchByIngredientsPresenter.onDestroy();
    }
}