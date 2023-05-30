package com.example.mealsapp.ui.auth.splash.presenter;

import com.example.mealsapp.Repo.Repo;
import com.example.mealsapp.model.pojo.user.User;
import com.example.mealsapp.ui.auth.splash.view.SplashView;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashPresenterImpl implements SplashPresenter {
    private static final String TAG = "SplashPresenterImpl";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SplashView splashView;
    private Repo repo;

    public SplashPresenterImpl(SplashView splashView, Repo repo) {
        this.splashView = splashView;
        this.repo = repo;
    }

    public boolean getIsLoggedInFlag() {
        return repo.getIsLoggedInFlag();
    }

    public String getUserID() {
        return repo.getUserID();
    }

    @Override
    public void getUserDetails(String id) {
        repo.getUserDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(User user) {
                        splashView.navigateToHome(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        splashView.showError(e.getMessage());
                    }
                });
    }

    @Override
    public void onDestroy() {
        if(!compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }
}
