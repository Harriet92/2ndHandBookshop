package com.szlif.bookshop;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.szlif.bookshop.models.User;

public class AppData {

    private static final String PREF_FILE_NAME = "com.szlif.bookshop.data";
    private static final String PASSWORD_KEY = "com.szlif.bookshop.password";
    private static final String LOGIN_KEY = "com.szlif.bookshop.login";

    public static String token;
    public static User user;

    private static SharedPreferences sharedPref;

    public static void rememberCredentials(String login, String password, Context context) {

        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(AppData.PASSWORD_KEY, password);
        editor.putString(AppData.LOGIN_KEY, login);
        editor.apply();

    }

    public static void forgetCredentials(Context context) {

        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(AppData.PASSWORD_KEY);
        editor.remove(AppData.LOGIN_KEY);
        editor.apply();

    }

    public static String getPassword(Context context) {

        SharedPreferences pref = getSharedPref(context);
        return pref.getString(AppData.PASSWORD_KEY, null);

    }

    public static String getLogin(Context context) {

        SharedPreferences pref = getSharedPref(context);
        return pref.getString(AppData.LOGIN_KEY, null);

    }

    private static SharedPreferences getSharedPref(Context context) {

        if(AppData.sharedPref == null) {
            AppData.sharedPref = context.getSharedPreferences(AppData.PREF_FILE_NAME, Context.MODE_PRIVATE);
        }

        return AppData.sharedPref;

    }

}
