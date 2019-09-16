package com.app.datacontorller.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.text.SimpleDateFormat;
import java.util.Date;


public class LoginPrefManager {

    private final Context _context;


    private SharedPreferences pref;
    private Editor editor;

    private static final String PREF_NAME = "Monitor";

    private SimpleDateFormat SERVER_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");

    public LoginPrefManager(Context context) {
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }


    public void setStringValue(String keyName, String value) {
        pref.edit().putString(keyName, value).apply();

    }

    public String getStringValue(String keyName) {

        return pref.getString(keyName, "");

    }


    public void setAppOpen(String status) {
        pref.edit().putString("app_open", status).apply();
    }


    public String getAppopen() {
        return pref.getString("app_open", "1");
    }


    public void setOnstartTime(String time) {
        pref.edit().putString("on_start_time", time).apply();
    }

    public String getOnstartTime() {
        return pref.getString("on_start_time", "0");
    }

    public void setOffstartTime(String time) {
        pref.edit().putString("off_start_time", time).apply();
    }

    public String getOffstartTime() {
        return pref.getString("off_start_time", "0");
    }

    public void setOnendTime(String time) {
        pref.edit().putString("on_end_time", time).apply();
    }

    public String getOnEndTime() {
        return pref.getString("on_end_time", "0");
    }


    public void setOffendTime(String time) {
        pref.edit().putString("off_end_time", time).apply();
    }

    public String getOffendtTime() {
        return pref.getString("off_end_time", "0");
    }


    public String getDate(Date date) {
        return SERVER_FORMAT.format(date);
    }


    public void setFirstTimeOpen(boolean status) {
        pref.edit().putBoolean("auto_start", status).apply();
    }

    public boolean getFirstTImeOpen() {
        return pref.getBoolean("auto_start", false);
    }


}
