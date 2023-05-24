package com.example.mealsapp.utils;

import android.content.Context;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class Helper {
    private static KProgressHUD hud;

    public static void showProgress(Context context) {
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public static void hideProgress(Context context) {
        hud.dismiss();
    }

}
