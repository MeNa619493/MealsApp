package com.example.mealsapp.ui.auth.login.presenter;

import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.model.pojo.user.User;
import com.example.mealsapp.ui.auth.login.view.LoginView;
import com.example.mealsapp.utils.AuthHelper;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView loginView;
    private Repo repo;
    private Disposable disposable1;
    private Disposable disposable2;

    public LoginPresenterImpl(LoginView loginView, Repo repo) {
        this.loginView = loginView;
        this.repo = repo;
    }

    @Override
    public void validateUserInput(User user) {
        String state = AuthHelper.validateUserInputRegex(user.getEmail(), user.getPassWord());
        if (state.equals(AuthHelper.SUCCESS)) {

            disposable1 = repo.signIn(user).flatMap(id -> {
                return repo.getUserDetails(id);
            }).subscribeOn(Schedulers.io()).subscribe(loggedInUser -> {
                Completable first = repo.savePlannedMealsToDatabase(loggedInUser);
                Completable second = repo.saveFavoritesToDatabase(loggedInUser);
                disposable2 = first.andThen(second).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                    repo.putIsLoggedInFlag(true);
                    repo.putUserID(loggedInUser.getId());
                    loginView.navigateToHome(loggedInUser);
                });
            }, throwable -> {
                loginView.showError(throwable.getMessage());
            });

        } else {
            loginView.showError(state);
        }
    }

    @Override
    public void onDestroy() {
        disposable1.dispose();
        disposable2.dispose();
    }
}
