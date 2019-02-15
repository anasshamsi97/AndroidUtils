package com.example.myutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.example.myutils.Utils.Utils;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View parentLayout = findViewById(android.R.id.content);
        Utils.showIndefiniteSnackBarWithAction(parentLayout, "Hello World");
        Utils.showSnackBar(parentLayout,"Hello World");
        boolean isTablet = Utils.isTablet();
        Log.d(TAG, "onCreate: "+isTablet);
        Utils.vibrate(1500,this);
    }
}
