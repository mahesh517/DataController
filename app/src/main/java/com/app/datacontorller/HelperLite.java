package com.app.datacontorller;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;

import org.greenrobot.greendao.database.Database;

import io.fabric.sdk.android.Fabric;

public class HelperLite extends Application {

    private static Context applicationContext = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        applicationContext = getApplicationContext();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


    }

    public static Context getContext() {
        return applicationContext;
    }

}
