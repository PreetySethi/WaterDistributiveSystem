package com.example.wds;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;



public class SharedPref {

    private static final String APP_KEY = "app_name";
   // private static final String USER_ID = "cus_id";
    private static final String USER_auth = "auth_token";

    public static void SaveUSER_auth(String auth, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_auth, auth);
        editor.apply();
    }

    public static String getUSER_auth(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(USER_auth, "");
    }
}
