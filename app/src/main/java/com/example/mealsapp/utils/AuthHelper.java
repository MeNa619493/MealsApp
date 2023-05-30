package com.example.mealsapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.mealsapp.R;
import com.example.mealsapp.model.pojo.user.User;
import com.example.mealsapp.ui.home.HomeActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthHelper {
    public static final String SUCCESS = "SUCCESS";
    public static final String USERS = "users";
    public static User currentUser;

    public static void assignCurrentUser(Context context, User user) {
        currentUser = user;
        navigateToHomeActivity(context);
    }

    public static void navigateToHomeActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String validateUserInputRegex(String email, String password) {
        Pattern emailRegex = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
        Pattern passwordPattern = Pattern.compile("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=_])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$");
        Matcher emailMatcher = emailRegex.matcher(email);
        Matcher passMatcher = passwordPattern.matcher(password);

        if (!emailMatcher.matches()) {
            return  "Please enter valid email";
        } else if (password.length() < 8 || !passMatcher.matches()) {
            return "Please enter valid password";
        } else {
            return SUCCESS;
        }
    }

    public static void closeKeyboard(Activity activity) {
        InputMethodManager imm = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        }
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //requireContext()
    public static void showErrorToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showErrorSnackBar(View view, String message) {
        Snackbar snackBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackBar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorSnackBarError));
        snackBar.show();
    }
}
