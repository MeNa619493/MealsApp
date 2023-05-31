package com.example.mealsapp.ui.auth.choose.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.FragmentChooseBinding;
import com.example.mealsapp.model.pojo.user.User;
import com.example.mealsapp.ui.auth.choose.presenter.ChoosePresenter;
import com.example.mealsapp.ui.auth.choose.presenter.ChoosePresenterImpl;
import com.example.mealsapp.ui.home.HomeActivity;
import com.example.mealsapp.utils.AuthHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class ChooseFragment extends Fragment implements ChooseView {
    private FragmentChooseBinding binding;
    private static final String TAG = "ChooseFragment";
    private GoogleSignInClient googleSignInClient;
    private final int REQUEST_CODE = 100;
    private FirebaseAuth mAuth;
    private ChoosePresenter choosePresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        choosePresenter = new ChoosePresenterImpl(this, ((MealsApplication) getActivity().getApplication()).getRepo());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChooseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createRequest();
        observeNavigation();
        observeGoogleButton();
    }

    private void observeNavigation() {
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(ChooseFragmentDirections.actionChooseFragmentToRegisterFragment());
            }
        });

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(ChooseFragmentDirections.actionChooseFragmentToLoginFragment());
            }
        });

        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(requireContext()).setTitle("Wait! Are you sure?")
                .setMessage("You'll miss out on personalized content and saving our delicious recipes.")
                .setIcon(R.drawable.login)
                .setCancelable(false)
                .setPositiveButton("YES, I'M SURE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        choosePresenter.putIsLoggedInFlag(false);
                        AuthHelper.navigateToHomeActivity(getContext());
                    }
                })
                .setNegativeButton("No, GO BACK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create()
                .show();
    }

    private void observeGoogleButton() {
        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void createRequest() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithGoogle:success");
                    if (mAuth.getCurrentUser() != null) {
                        User user = new User();
                        user.setId(mAuth.getCurrentUser().getUid());
                        user.setUserName(mAuth.getCurrentUser().getDisplayName());
                        user.setEmail(mAuth.getCurrentUser().getEmail());
                        choosePresenter.checkIfUserExist(user);
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithGoogle:failure", task.getException());
                    Toast.makeText(getActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void navigateToHome(User user) {
        Log.d(TAG, "showSuccess: ");
        AuthHelper.assignCurrentUser(requireContext(), user);
    }

    @Override
    public void showError(String message) {
        Log.d(TAG, "showError: " + message);
        AuthHelper.showErrorToast(requireContext(), message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        choosePresenter.onDestroy();
    }

}