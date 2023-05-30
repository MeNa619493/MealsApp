package com.example.mealsapp.ui.auth.login.view;

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
import com.example.mealsapp.databinding.FragmentLoginBinding;
import com.example.mealsapp.model.pojo.user.User;
import com.example.mealsapp.ui.auth.login.presenter.LoginPresenter;
import com.example.mealsapp.ui.auth.login.presenter.LoginPresenterImpl;
import com.example.mealsapp.utils.AuthHelper;

public class LoginFragment extends Fragment implements LoginView {
    private LoginPresenter loginPresenter;
    private static final String TAG = "LoginFragment";
    private FragmentLoginBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPresenter = new LoginPresenterImpl(this, ((MealsApplication) getActivity().getApplication()).getRepo());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
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
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthHelper.closeKeyboard(getActivity());
                if (isValid()) {
                    User user = new User();
                    user.setEmail(binding.etEmail.getText().toString().trim());
                    user.setPassWord(binding.etPass.getText().toString().trim());

                    loginPresenter.validateUserInput(user);
                }
            }
        });
    }

    private Boolean isValid() {
        boolean isSuccess = false;
        if (binding.etEmail.getText().toString().trim().isEmpty()) {
            AuthHelper.showErrorSnackBar(getView(),"Please Enter the email");
        } else if (binding.etPass.getText().toString().trim().isEmpty()) {
            AuthHelper.showErrorSnackBar(getView(),"Please Enter the password");
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
        loginPresenter.onDestroy();
    }
}