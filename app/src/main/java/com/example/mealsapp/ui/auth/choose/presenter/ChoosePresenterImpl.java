package com.example.mealsapp.ui.auth.choose.presenter;

import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.model.pojo.user.User;
import com.example.mealsapp.ui.auth.choose.view.ChooseView;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChoosePresenterImpl implements ChoosePresenter {
    private static final String TAG = "ChoosePresenterImpl";
    private Disposable disposable1;
    private Disposable disposable2;
    private ChooseView chooseView;
    private Repo repo;

    public ChoosePresenterImpl(ChooseView chooseView, Repo repo) {
        this.chooseView = chooseView;
        this.repo = repo;
    }

    @Override
    public void checkIfUserExist(User user) {
        disposable1 = repo.getUserDetails(user.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loggedInUser -> {
                    if (loggedInUser != null) {
                        Completable first = repo.savePlannedMealsToDatabase(loggedInUser);
                        Completable second = repo.saveFavoritesToDatabase(loggedInUser);
                        disposable2 = first.andThen(second).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                            repo.putIsLoggedInFlag(true);
                            repo.putUserID(loggedInUser.getId());
                            chooseView.navigateToHome(loggedInUser);
                        });
                    } else {
                        disposable2 = repo.addUserToFirebaseCollection(user)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(uploadedUser -> {
                                    repo.putIsLoggedInFlag(true);
                                    repo.putUserID(loggedInUser.getId());
                                    chooseView.navigateToHome(loggedInUser);
                                }, throwable -> {
                                    chooseView.showError(throwable.getMessage());
                                });
                    }
                }, throwable -> {
                    chooseView.showError(throwable.getMessage());
                });
    }

    public void putIsLoggedInFlag(boolean isLoggedIn) {
        repo.putIsLoggedInFlag(isLoggedIn);
    }

    @Override
    public void onDestroy() {
        disposable1.dispose();
        disposable2.dispose();
    }
}
