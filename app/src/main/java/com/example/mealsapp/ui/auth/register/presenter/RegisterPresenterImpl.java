package com.example.mealsapp.ui.auth.register.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.model.pojo.user.User;
import com.example.mealsapp.ui.auth.register.view.RegisterView;
import com.example.mealsapp.utils.AuthHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterPresenterImpl implements RegisterPresenter {
    private static final String TAG = "RegisterPresenterImpl";
    private Disposable disposable;
    private RegisterView registerView;
    private Repo repo;

    public RegisterPresenterImpl(RegisterView registerView, Repo repo) {
        this.registerView = registerView;
        this.repo = repo;
    }

    public void validateUserInput(User user) {
        String state = AuthHelper.validateUserInputRegex(user.getEmail(), user.getPassWord());
        if (state.equals(AuthHelper.SUCCESS)) {
            disposable = repo.signUp(user).flatMap(registeredUser -> {
                        return repo.addUserToFirebaseCollection(registeredUser);
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(loggedInUser -> {
                        repo.putIsLoggedInFlag(true);
                        repo.putUserID(loggedInUser.getId());
                        registerView.navigateToHome(loggedInUser);
                    }, throwable -> {
                        registerView.showError(throwable.getMessage());
                    });
        } else {
            registerView.showError(state);
        }
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }
}
