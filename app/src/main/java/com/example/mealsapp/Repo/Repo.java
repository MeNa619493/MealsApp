package com.example.mealsapp.Repo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealsapp.Repo.local.LocalDataSource;
import com.example.mealsapp.Repo.remote.RemoteDataSource;
import com.example.mealsapp.model.data.local.SharedPref;
import com.example.mealsapp.model.pojo.category.CategoryResponse;
import com.example.mealsapp.model.pojo.country.CountryResponse;
import com.example.mealsapp.model.pojo.ingredient.IngredientResponse;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.MealResponse;
import com.example.mealsapp.model.pojo.meal.PlannedMeal;
import com.example.mealsapp.model.pojo.user.User;
import com.example.mealsapp.utils.AuthHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class Repo {
    private static final String TAG = "Repo";
    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;
    private SharedPref sharedPref;
    private static Repo instance;
    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;

    public Repo(LocalDataSource localDataSource, RemoteDataSource remoteDataSource, SharedPref sharedPref) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.sharedPref = sharedPref;
        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();
    }

    public static synchronized Repo getInstance(LocalDataSource localDataSource, RemoteDataSource remoteDataSource, SharedPref sharedPref) {
        if (instance == null) {
            instance = new Repo(localDataSource, remoteDataSource, sharedPref);
        }
        return instance;
    }

    public Single<MealResponse> getRandomMeal() {
        return remoteDataSource.getRandomMeal();
    }

    public Single<MealResponse> getMealById(String id) {
        return remoteDataSource.getMealById(id);
    }

    public Single<MealResponse> getSuggestionMeals(char c) {
        return remoteDataSource.getYouMightLikeMeal(c);
    }

    public Single<CategoryResponse> getCategories() {
        return remoteDataSource.getCategories();
    }

    public Single<CountryResponse> getCountries() {
        return remoteDataSource.getCountries();
    }

    public Single<IngredientResponse> getIngredients() {
        return remoteDataSource.getIngredients();
    }

    public Single<MealResponse> searchByCategory(String categoryName) {
        return remoteDataSource.searchByCategory(categoryName);
    }

    public Single<MealResponse> searchByCountry(String countryName) {
        return remoteDataSource.searchByCountry(countryName);
    }

    public Single<MealResponse> searchByIngredient(String ingredientName) {
        return remoteDataSource.searchByIngredient(ingredientName);
    }

    public Single<MealResponse> searchByName(String name) {
        return remoteDataSource.searchByName(name);
    }

    public Completable addFavorite(Meal meal) {
        return localDataSource.insertMeal(meal);
    }

    public Completable insertAllFavorites(List<Meal> meals) {
        return localDataSource.insertAllMeals(meals);
    }

    public Single<List<Meal>> getFavouriteMeals() {
        return localDataSource.getMeals();
    }

    public Completable deleteFavorite(Meal meal) {
        return localDataSource.deleteMeal(meal);
    }

    public Completable deleteAllMeals() {
        return localDataSource.deleteAllMeals();
    }

    public Completable insertPlannedMeal(PlannedMeal plannedMeal) {
        return localDataSource.insertPlannedMeal(plannedMeal);
    }

    public Completable insertAllPlannedMeals(List<PlannedMeal> plannedMeals) {
        return localDataSource.insertAllPlannedMeals(plannedMeals);
    }

    public Single<List<PlannedMeal>> getAllPlannedMeals() {
        return localDataSource.getAllPlannedMeals();
    }

    public Completable deletePlannedMeal(PlannedMeal plannedMeal) {
        return localDataSource.deletePlannedMeal(plannedMeal);
    }

    public Completable deleteAllPlannedMeal() {
        return localDataSource.deleteAllPlannedMeal();
    }

    public void putIsLoggedInFlag(boolean isLoggedIn) {
        sharedPref.putIsLoggedInFlag(isLoggedIn);
    }

    public boolean getIsLoggedInFlag() {
        return sharedPref.getIsLoggedInFlag();
    }

    public void putUserID(String id) {
        sharedPref.putUserID(id);
    }

    public String getUserID() {
        return sharedPref.getUserID();
    }

    public Single<User> signUp(User user){
        return Single.create(emitter -> {
            mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassWord())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                user.setId(mAuth.getUid());
                                emitter.onSuccess(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.d(TAG, "createUserWithEmail:failure", task.getException());
                                emitter.onError(new Exception("create User With Email failed"));
                            }
                        }
                    });
        });
    }

    public Single<String> signIn(User user) {
        return Single.create(emitter -> {
            mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassWord())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                emitter.onSuccess(mAuth.getUid());
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                emitter.onError(new Exception("sign In With Email failed"));
                            }
                        }
                    });
        });
    }

    public Single<User> getUserDetails(String id) {
        return Single.create(emitter -> {
            mFireStore.collection(AuthHelper.USERS).document(id).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "Document exist!");
                                    User loggedInUser = document.toObject(User.class);
                                    emitter.onSuccess(loggedInUser);
                                } else {
                                    Log.d(TAG, "Document does not exist!");
                                    emitter.onSuccess(null);
                                }
                            } else {
                                Log.d(TAG, "Failed with: ", task.getException());
                                emitter.onError(new Exception("getting user data failed"));
                            }
                        }
                    });
        });
    }

    public Completable savePlannedMealsToDatabase(User user) {
        return insertAllPlannedMeals(user.getPlanned()).subscribeOn(Schedulers.io());
    }

    public Completable saveFavoritesToDatabase(User user) {
            return  insertAllFavorites(user.getFavorites()).subscribeOn(Schedulers.io());
    }

    public Single<User> addUserToFirebaseCollection(User user) {
        return Single.create(emitter -> {
            mFireStore.collection(AuthHelper.USERS).document(user.getId()).set(user, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User data uploaded to firestore.");
                                emitter.onSuccess(user);
                            } else {
                                Log.d(TAG, "Failed with: ", task.getException());
                                emitter.onError(new Exception("upload user data failed"));
                            }
                        }
                    });
        });
    }

}
