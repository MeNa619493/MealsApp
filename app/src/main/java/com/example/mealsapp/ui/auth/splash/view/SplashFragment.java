package com.example.mealsapp.ui.auth.splash.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.databinding.FragmentSplashBinding;
import com.example.mealsapp.model.pojo.user.User;
import com.example.mealsapp.ui.auth.splash.presenter.SplashPresenter;
import com.example.mealsapp.ui.auth.splash.presenter.SplashPresenterImpl;
import com.example.mealsapp.utils.AuthHelper;

public class SplashFragment extends Fragment implements SplashView {
    private static final String TAG = "SplashFragment";
    private FragmentSplashBinding binding;
    private SplashPresenter splashPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashPresenter = new SplashPresenterImpl(this, ((MealsApplication) getActivity().getApplication()).getRepo());
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (splashPresenter.getIsLoggedInFlag()){
                    splashPresenter.getUserDetails(splashPresenter.getUserID());
                } else {
                    Navigation.findNavController(view).navigate(SplashFragmentDirections.actionSplashFragmentToChooseFragment());
                }
            }
        }, 3000);
    }

    @Override
    public void navigateToHome(User user) {
        Log.d(TAG, "showSuccess: ");
        AuthHelper.assignCurrentUser(requireContext(), user);
    }

    @Override
    public void showError(String message) {
        AuthHelper.showErrorToast(requireContext(), message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        splashPresenter.onDestroy();
    }
}