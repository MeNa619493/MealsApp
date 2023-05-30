package com.example.mealsapp.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mealsapp.MealsApplication;
import com.example.mealsapp.R;
import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.databinding.ActivityHomeBinding;
import com.example.mealsapp.model.pojo.user.User;
import com.example.mealsapp.ui.auth.MainActivity;
import com.example.mealsapp.utils.AuthHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Repo repo;
    private FirebaseFirestore mFireStore;
    private static final String TAG = "HomeActivity";
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mFireStore = FirebaseFirestore.getInstance();
        repo = ((MealsApplication) getApplication()).getRepo();
        if (repo.getIsLoggedInFlag()) {
            binding.tvUsername.setText(AuthHelper.currentUser.getUserName());
        }

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repo.getIsLoggedInFlag()) {
                    AuthHelper.currentUser.setId(repo.getUserID());
                     disposable = repo.getAllPlannedMeals().observeOn(Schedulers.io())
                            .flatMap(plannedMeals -> {
                                AuthHelper.currentUser.setPlanned(plannedMeals);
                                repo.deleteAllPlannedMeal();
                                return repo.getFavouriteMeals();
                            })
                            .subscribeOn(Schedulers.io())
                            .flatMapCompletable(meals -> {
                                AuthHelper.currentUser.setFavorites(meals);
                                return repo.deleteAllMeals();
                            }).subscribeOn(Schedulers.io())
                             .subscribe(() -> {
                                 updateUserInCloud(AuthHelper.currentUser);
                             });
                }
            }
        });


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(binding.navigationBar, navController);
    }

    private void updateUserInCloud(User userInfo) {
        mFireStore.collection(AuthHelper.USERS).document(userInfo.getId()).set(userInfo, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "UpdateUserDocument: FirebaseFirestore");
                            navigateToMainActivity();
                        } else {
                            Log.d(TAG, "FirebaseFirestore :failure", task.getException());
                            AuthHelper.showErrorToast(HomeActivity.this, "Authentication failed.");
                        }
                    }
                });
    }

    public void navigateToMainActivity() {
        repo.putIsLoggedInFlag(false);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}