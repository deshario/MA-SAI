package com.deshario.agriculture.Config;

import android.app.Application;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.deshario.agriculture.Models.Category;


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
