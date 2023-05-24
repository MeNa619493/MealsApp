package com.example.mealsapp.ui.home.details.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.FragmentDetailsBinding;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.ui.home.details.presnter.DetailsPresenter;
import com.example.mealsapp.ui.home.details.presnter.DetailsPresenterImpl;
import com.example.mealsapp.ui.home.home.view.HomeFragmentDirections;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

public class DetailsFragment extends Fragment implements DetailsView{
    public static final String TAG = "DetailsFragment";
    private FragmentDetailsBinding binding;
    private DetailsPresenter detailsPresenter;
    private Meal meal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsPresenter = new DetailsPresenterImpl(this, ((MealsApplication)getActivity().getApplication()).getRepo());
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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        meal = DetailsFragmentArgs.fromBundle(getArguments()).getMeal();

        initUi();
        initVideo();
        initSaveButtonUi();
        observeSaveButton();
        observeShareButton();
        observeBackButton();
    }

    private void initUi() {
        binding.tvMealName.setText(meal.getStrMeal());
        binding.tvMealCategory.setText(meal.getStrCategory());
        Glide.with(getContext())
                .load(meal.getStrMealThumb())
                .placeholder(R.drawable.placeholder)
                .into(binding.ivMealImage);
    }

    private void initVideo() {
        getLifecycle().addObserver(binding.epVideoView);

        binding.epVideoView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = getVideoId(meal.getStrYoutube());
                youTubePlayer.loadVideo(videoId, 0);
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
                if (meal.isFavorite()){
                    meal.setFavorite(false);
                    detailsPresenter.deleteMeal(meal);
                } else {
                    meal.setFavorite(true);
                    detailsPresenter.addFavorite(meal);
                }
            }
        });
    }

    private void initSaveButtonUi() {
        if (meal.isFavorite()){
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
    public void mealAddedToFavoriteSuccessfully(Meal meal) {
        initSaveButtonUi();
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
        binding = null;
        detailsPresenter.onDestroy();
    }
}