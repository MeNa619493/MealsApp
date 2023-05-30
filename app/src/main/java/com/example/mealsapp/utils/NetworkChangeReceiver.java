package com.example.mealsapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NetworkChangeReceiver extends BroadcastReceiver {
    public static final String TAG = "NetworkChangeReceiver";
    private static MutableLiveData<Boolean> _isConnected = new MutableLiveData<>();

    public static LiveData<Boolean> getIsConnected() {
        return _isConnected;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.e(TAG, "onReceive");
        String action = intent.getAction();
        if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            _isConnected.setValue(isNetworkAvailable(context));
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

}
