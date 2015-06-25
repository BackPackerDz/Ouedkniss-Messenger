package com.squalala.ouedknissmessenger.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.squalala.ouedknissmessenger.utils.Preferences;

/**
 * Created by Back Packer
 * Date : 25/06/15
 */
public class BaseActivtiy extends AppCompatActivity {

    protected Preferences preferences;
    private static final String TAG = BaseActivtiy.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new Preferences(this);
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        super.onPause();

        preferences.setInApp(false);
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();

        preferences.setInApp(true);
    }
}
