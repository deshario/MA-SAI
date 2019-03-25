package com.deshario.agriculture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static final String CONFIG_NAME = "passcodeConfig";
    private static final String INITIAL_DATA = "IsFirstData";
    private static final String RESET_LOGIN = "resetLogin";
    private static final String PASSCODE = "passcode";

    @SuppressLint("CommitPrefEdits")
    SharedPreference(Context context){
        pref = context.getSharedPreferences(CONFIG_NAME, 0);
        editor = pref.edit();
    }

    public void setLoginPassword(String passcode) {
        editor.putString(PASSCODE,passcode);
        editor.commit();
    }

    public String getLoginPassword() {
        return pref.getString(PASSCODE, null);
    }

    public void setInitialLaunch(boolean isInitial) {
        editor.putBoolean(INITIAL_DATA, isInitial);
        editor.commit();
    }

    public boolean isInitialLaunch() {
        return pref.getBoolean(INITIAL_DATA, true);
    }

    public void resetPassword(boolean isReset) {
        editor.putBoolean(RESET_LOGIN, isReset);
        editor.commit();
    }

    public boolean isResetPassword() {
        return pref.getBoolean(RESET_LOGIN, true);
    }
}