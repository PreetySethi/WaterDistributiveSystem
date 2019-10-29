package com.example.wds;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    private final String INTRO = "intro";
    private final String FNAME = "first_name";
    private final String LNAME = "last_name";
    private final String PREFERENCE_NAME_SESSION="session_user";
   // private final String MOB = "MOB";
    private SharedPreferences app_prefs;
    private Context context;

    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences("shared",
                Context.MODE_PRIVATE);
        this.context = context;
    }

    public void putIsLogin(boolean loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(INTRO, loginorout);
        edit.commit();
    }
   public boolean getIsLogin() {
        return app_prefs.getBoolean(INTRO, true);
    }

    public void putFName(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(FNAME, loginorout);
        edit.commit();
    }
    public String getFName() {
        return app_prefs.getString(FNAME, "");
    }

    public void putLName(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(LNAME, loginorout);
        edit.commit();
    }
    public String getLName() {
        return app_prefs.getString(LNAME, "");
    }
 }





