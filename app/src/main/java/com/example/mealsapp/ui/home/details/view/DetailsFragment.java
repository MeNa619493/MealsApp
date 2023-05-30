package com.example.mealsapp.ui.home.details.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.FragmentDetailsBinding;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.PlannedMeal;
import com.example.mealsapp.ui.home.HomeActivity;
import com.example.mealsapp.ui.home.details.presnter.DetailsPresenter;
import com.example.mealsapp.ui.home.details.presnter.DetailsPresenterImpl;
import com.example.mealsapp.utils.DateFormatter;
import com.example.mealsapp.utils.NetworkChangeReceiver;
import com.example.mealsapp.utils.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.type.DateTime;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DetailsFragment extends Fragment implements DetailsView {
    public static final String TAG = "DetailsFragment";
    private FragmentDetailsBinding binding;
    private DetailsPresenter detailsPresenter;
    private Meal meal;
    private ArrayList<String> ingredients;
    private Snackbar snackbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsPresenter = new DetailsPresenterImpl(this, ((MealsApplication) getActivity().getApplication()).getRepo());
        meal = DetailsFragmentArgs.fromBundle(getArguments()).getMeal();
        if (meal.getStrArea() == null) {
            detailsPresenter.getMealById(meal.getIdMeal());
        }
        ingredients = Utility.getMealIngredients(meal);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
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

        initUi();
        initVideo();
        initSaveButtonUi();
        observeSaveButton();
        observeShareButton();
        observeBackButton();
        observeCalendarButton();

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
        snackbar = Snackbar.make(binding.getRoot(), "No network connection", Snackbar.LENGTH_INDEFINITE);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorSnackBarError));
        snackbar.show();
    }

    private void hideSnackbar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    public void assignMeal(Meal meal) {
        this.meal = meal;
        ingredients = Utility.getMealIngredients(this.meal);
        initUi();
    }

    public void assignMealFailure(String message) {
        Toast.makeText(getContext(),
                "Error, please try again later!!",
                Toast.LENGTH_SHORT).show();
    }

    private void initUi() {
        binding.tvMealName.setText(meal.getStrMeal());
        binding.tvMealCategory.setText(meal.getStrCategory());
        binding.tvMealInstructions.setText(meal.getStrInstructions());
        binding.tvMealArea.setText(meal.getStrArea());
        Glide.with(requireContext())
                .load(meal.getStrMealThumb())
                .placeholder(R.drawable.placeholder)
                .into(binding.ivMealImage);
        initIngredientsRecyclerview();
    }

    private void initIngredientsRecyclerview() {
        Log.i(TAG, "initIngredientsRecyclerview: " + ingredients.size());
        binding.rvIngredients.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvIngredients.setLayoutManager(layoutManager);
        MealIngredientsAdapter adapter = new MealIngredientsAdapter(getContext());
        adapter.submitList(ingredients);
        binding.rvIngredients.setAdapter(adapter);
    }

    private void initVideo() {
        getLifecycle().addObserver(binding.epVideoView);

        binding.epVideoView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = getVideoId(meal.getStrYoutube());
                youTubePlayer.cueVideo(videoId, 0);
            }
        });
    }

    private String getVideoId(String link) {
        if (link != null && link.split("\\?v=").length > 1)
            return link.split("\\?v=")[1];
        else return "";
    }

    private void observeSaveButton() {
        binding.ivSaveMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailsPresenter.getIsLoggedInFlag()) {
                    if (meal.isFavorite()) {
                        meal.setFavorite(false);
                        detailsPresenter.deleteMeal(meal);
                    } else {
                        meal.setFavorite(true);
                        detailsPresenter.addFavorite(meal);
                    }
                } else {
                    showAlertDialog();
                }
            }
        });
    }

    private void observeCalendarButton() {
        binding.ivAddPlanMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailsPresenter.getIsLoggedInFlag()) {
                    DialogFragment datePickerFragment = new DatePickerFragment(detailsPresenter, meal);
                    datePickerFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
                } else {
                    showAlertDialog();
                }
            }
        });
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

    private void initSaveButtonUi() {
        if (meal.isFavorite()) {
            binding.ivSaveMeal.setImageDrawable(getContext().getResources().getDrawable(R.drawable.bookmark_filled));
        } else {
            binding.ivSaveMeal.setImageDrawable(getContext().getResources().getDrawable(R.drawable.bookmark));
        }
    }

    private void observeBackButton() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        });
    }

    private void observeShareButton() {
        binding.ivShareMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this meal");
                intent.putExtra(Intent.EXTRA_TEXT, meal.getStrMeal() + " for " + meal.getStrCategory());
                intent.putExtra(Intent.EXTRA_STREAM, meal.getStrMealThumb());
                startActivity(Intent.createChooser(intent, "Share with"));
            }
        });
    }

    @Override
    public void mealAddedToWeekPlanSuccessfully() {
        Toast.makeText(getContext(),
                "Meal added Successfully",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mealAddedToWeekPlanFailure() {
        Toast.makeText(getContext(),
                "Error, please try again later!!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mealAddedToFavoriteSuccessfully(Meal meal) {
        initSaveButtonUi();
        Toast.makeText(getContext(),
                "Meal Saved Successfully",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mealAddedToFavoriteFailure() {
        Toast.makeText(getContext(),
                "Error, please try again later!!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mealDeletedSuccessfully(Meal meal) {
        initSaveButtonUi();
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
        binding.epVideoView.release();
        hideSnackbar();
        binding = null;
        detailsPresenter.onDestroy();
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private DetailsPresenter detailsPresenter;
        private Meal meal;

        public DatePickerFragment(DetailsPresenter detailsPresenter, Meal meal) {
            this.detailsPresenter = detailsPresenter;
            this.meal = meal;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
            c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 6);
            datePickerDialog.getDatePicker().setMaxDate(c.getTime().getTime());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            PlannedMeal plannedMeal = new PlannedMeal(meal.getIdMeal(), meal, DateFormatter.getString(year, month, day));
            Log.i(TAG, "onDateSet: " + DateFormatter.getString(year, month, day));
            detailsPresenter.addPlannedMeal(plannedMeal);
        }
    }
}