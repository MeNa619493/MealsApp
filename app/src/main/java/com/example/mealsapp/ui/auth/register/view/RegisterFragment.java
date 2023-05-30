package com.example.mealsapp.ui.auth.register.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.databinding.FragmentRegisterBinding;
import com.example.mealsapp.model.pojo.user.User;
import com.example.mealsapp.ui.auth.register.presenter.RegisterPresenter;
import com.example.mealsapp.ui.auth.register.presenter.RegisterPresenterImpl;
import com.example.mealsapp.utils.AuthHelper;

public class RegisterFragment extends Fragment implements RegisterView {
    private RegisterPresenter registerPresenter;
    private FragmentRegisterBinding binding;
    private static final String TAG = "RegisterFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerPresenter = new RegisterPresenterImpl(this, ((MealsApplication) getActivity().getApplication()).getRepo());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observeRegisterButton();
        observeNavigation();
    }

    private void observeNavigation() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        });
    }

    private void observeRegisterButton() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthHelper.closeKeyboard(getActivity());
                if (isValid()) {
                    User user = new User();
                    user.setUserName(binding.etUsername.getText().toString().trim());
                    user.setEmail(binding.etEmail.getText().toString().trim());
                    user.setPassWord(binding.etPass.getText().toString().trim());

                    registerPresenter.validateUserInput(user);
                }
            }
        });
    }

    private Boolean isValid() {
        boolean isSuccess = false;
        if (binding.etUsername.getText().toString().trim().isEmpty()) {
            AuthHelper.showErrorSnackBar(getView(), "Please Enter the username");
        } else if (binding.etEmail.getText().toString().trim().isEmpty()) {
            AuthHelper.showErrorSnackBar(getView(), "Please Enter the email");
        } else if (binding.etPass.getText().toString().trim().isEmpty()) {
            AuthHelper.showErrorSnackBar(getView(), "Please Enter the password");
        } else if (binding.etConfirmPass.getText().toString().trim().isEmpty()) {
            AuthHelper.showErrorSnackBar(getView(), "Please Enter the password");
        } else if (!binding.etConfirmPass.getText().toString().equals(binding.etPass.getText().toString())) {
            AuthHelper.showErrorSnackBar(getView(), "Please Enter the correct password");
        } else {
            isSuccess = true;
        }
        return isSuccess;
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
        registerPresenter.onDestroy();
    }
}