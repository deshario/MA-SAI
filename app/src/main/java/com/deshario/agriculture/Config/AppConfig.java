package com.deshario.agriculture.Config;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by Deshario on 6/29/2017.
 */

public class AppConfig extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
