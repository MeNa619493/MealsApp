package com.example.mealsapp.ui.home.calender.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.FragmentCalendarBinding;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.PlannedMeal;
import com.example.mealsapp.ui.home.calender.presenter.CalendarPresenter;
import com.example.mealsapp.ui.home.calender.presenter.CalendarPresenterImpl;
import com.example.mealsapp.ui.home.calender.view.CalendarFragmentDirections.ActionCalendarFragmentToDetailsFragment;
import com.example.mealsapp.utils.DateFormatter;
import com.example.mealsapp.utils.NetworkChangeReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment implements CalendarView{
    public static final String TAG = "CalendarFragment";
    private FragmentCalendarBinding binding;
    private CalendarPresenter calendarPresenter;
    private PlannedAdapter adapter;
    private List<PlannedMeal> meals;
    private Snackbar snackbar;
    private BottomNavigationView navBar;
    private Toolbar actionBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendarPresenter = new CalendarPresenterImpl(this, ((MealsApplication) getActivity().getApplication()).getRepo());
        Log.i(TAG, "onCreate CalendarFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
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

        observeCalendarButton();
        calendarPresenter.getAllPlannedMeals(DateFormatter.getString(new Date()));

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

    private void observeCalendarButton() {
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment(calendarPresenter);
                datePickerFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    public void showPlannedMeals(List<PlannedMeal> meals, String date) {
        binding.tvDay.setText(DateFormatter.getDayName(DateFormatter.getDateObject(date)) + " " + date);
        if (meals.size() > 0) {
            this.meals = meals;
            Log.i(TAG, "showPlannedMeals: " + meals.size());
            binding.rvPlannedMeals.setVisibility(View.VISIBLE);
            binding.ivNoDataFound.setVisibility(View.GONE);
            binding.rvPlannedMeals.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            binding.rvPlannedMeals.setLayoutManager(layoutManager);
            adapter = new PlannedAdapter(new PlannedAdapter.OnMealClickListener() {
                @Override
                public void onMealClick(Meal meal) {
                    Log.i(TAG, "onClick: show Planned details");
                    ActionCalendarFragmentToDetailsFragment action =
                            CalendarFragmentDirections.actionCalendarFragmentToDetailsFragment(meal);
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                }

                @Override
                public void onDeleteClick(PlannedMeal plannedMeal) {
                    Log.i(TAG, "onClick: remove from Planned meals");
                    calendarPresenter.deleteMeal(plannedMeal);
                }

                @Override
                public void onShareMealClick(PlannedMeal plannedMeal) {
                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE , plannedMeal.getMeal().getStrMeal());
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, "Enjoy a " + plannedMeal.getMeal().getStrMeal());
                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, System.currentTimeMillis());
                    if(intent.resolveActivity(getContext().getPackageManager()) != null){
                        startActivity(intent);
                    }else{
                        Toast.makeText(getContext(), "Please download calender app first!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            adapter.submitList(meals);
            binding.rvPlannedMeals.setAdapter(adapter);
        } else {
            showNoDataView();
        }
    }

    private void showNoDataView() {
        binding.rvPlannedMeals.setVisibility(View.GONE);
        binding.ivNoDataFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable throwable) {
        Toast.makeText(getContext(),
                "Error getting planned meals",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mealDeletedSuccessfully(PlannedMeal meal) {
        int pos = meals.indexOf(meal);
        meals.remove(meal);
        adapter.notifyItemRemoved(pos);
        Log.i(TAG, "showPlannedMeals: meal deleted " + meals.size());
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
        calendarPresenter.onDestroy();
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private CalendarPresenter calendarPresenter;

        public DatePickerFragment(CalendarPresenter calendarPresenter) {
            this.calendarPresenter = calendarPresenter;
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
            calendarPresenter.getAllPlannedMeals(DateFormatter.getString(year, month, day));
        }
    }
}