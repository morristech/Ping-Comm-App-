package com.yoscholar.ping.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by agrim on 24/10/16.
 */

public class AppPreference {

    //USER DETAILS (details of the logged in person)
    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String EMAIL = "email";
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String TOKEN = "token";


    public static void saveInt(Context context, String key, int value) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt(key, -1);
    }

    public static void saveString(Context context, String key, String value) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(key, "null");
    }

    public static void saveBoolean(Context context, String key, boolean value) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(key, false);
    }

    public static void logOut(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(IS_LOGGED_IN);
        editor.remove(EMAIL);
        editor.remove(USER_NAME);
        editor.remove(USER_ID);
        editor.remove(TOKEN);
        editor.commit();

    }
}
