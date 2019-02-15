package com.example.myutils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Muhammad Anas Shamsi on 2/13/19.
 */
@SuppressLint("MissingPermission")
public class Utils {

    private final static String TAG = "Utils";

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static SweetAlertDialog showSweetLoader(Context context, String title) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#5C6BC0"));
        sweetAlertDialog.setTitleText(title);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        return sweetAlertDialog;
    }

    public static String getDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(c.getTime());
    }

    /**
     * Location.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />}</p>
     * OR
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />}</p>
     */
    public static void getLocation(Context context, final CallbackLocation callbackLocation) {
        LocationRequest request = new LocationRequest();
        request.setNumUpdates(1);
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);
        final String[] latlng = new String[2];
        client.requestLocationUpdates(request, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                latlng[0] = String.valueOf(locationResult.getLastLocation().getLatitude());
                Log.d(TAG, "onLocationResult: " + latlng[0]);
                latlng[1] = String.valueOf(locationResult.getLastLocation().getLongitude());
                Log.d(TAG, "onLocationResult: " + latlng[1]);
                callbackLocation.locationResult(latlng);
            }
        }, null);
    }

    public static void toJSONArray(List<Object> objectList) {
        Gson gson = new Gson();
        String element = gson.toJson(
                objectList,
                new TypeToken<ArrayList<Object>>() {
                }.getType());
        try {
            JSONArray jsonArray = new JSONArray(element);
            Log.d(TAG, "toJSONArray: " + jsonArray.toString());
        } catch (JSONException e) {
            Log.d(TAG, "toJSONArray: " + e.getMessage());
        }
    }

    public static void showIndefiniteSnackBarWithAction(View parentLayout, String s) {
        Snackbar.make(parentLayout, s, Snackbar.LENGTH_INDEFINITE)
                .setAction("CLOSE", null)
                .setActionTextColor(Resources.getSystem().getColor(android.R.color.holo_red_light))
                .show();
    }

    public static void showSnackBar(View parentLayout, String s) {
        Snackbar.make(parentLayout, s, Snackbar.LENGTH_LONG).show();
    }

    private static boolean checkSimAvailability(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = 0;
        if (telMgr != null)
            simState = telMgr.getSimState();

        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                return false;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                // do something
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                // do something
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                // do something
                break;
            case TelephonyManager.SIM_STATE_READY:
                // do something
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                // do something
                break;
        }
        return true;
    }

    public interface CallbackLocation {
        void locationResult(String[] latlng);
    }

    public static boolean isTablet() {
        return (Resources.getSystem().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * Vibrate.
     * <p>Must hold {@code <uses-permission android:name="android.permission.VIBRATE" />}</p>
     */
    public static void vibrate(int milliSeconds, Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(milliSeconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(milliSeconds);
        }
    }

    /**
     * Hides the activity's action bar
     *
     * @param activity the activity
     */
    public static void hideActionBar(Activity activity) {
        // Call before calling setContentView();
        if (activity != null) {
            activity.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            if (activity.getActionBar() != null) {
                activity.getActionBar().hide();
            }
        }
    }

    /**
     * Sets the activity in Fullscreen mode
     *
     * @param activity the activity
     */
    public static void setFullScreen(Activity activity) {
        // Call before calling setContentView();
        activity.getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


}
