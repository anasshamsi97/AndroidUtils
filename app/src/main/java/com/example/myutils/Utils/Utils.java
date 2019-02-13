package com.example.myutils.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Muhammad Anas Shamsi on 2/13/19.
 */
public class Utils {

    private final static String TAG = "Utils";

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static SweetAlertDialog showSweetLoader(Context context, int dialogtype, String title) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#5C6BC0"));
        pDialog.setTitleText(title);
        pDialog.setCancelable(false);
        pDialog.show();

        return pDialog;
    }

    public static String getDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(c.getTime());
    }

    @SuppressLint("MissingPermission")
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
            Log.d(TAG, "toJSONArray: "+jsonArray.toString());
        } catch (JSONException e) {
            Log.d(TAG, "toJSONArray: "+e.getMessage());
        }
    }

    public interface CallbackLocation {
        void locationResult(String[] latlng);
    }
}
