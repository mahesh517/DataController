package com.app.datacontorller;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.app.datacontorller.Utils.LoginPrefManager;

public class BootUpReciever extends BroadcastReceiver {


    Handler handler;

    Runnable runnable;


    LoginPrefManager loginPrefManager;

    @Override
    public void onReceive(Context context, Intent intent) {


        loginPrefManager = new LoginPrefManager(context);
        if (intent != null) {
            PackageManager p = context.getPackageManager();

            loginPrefManager.setStringValue("app_open", "0");
            ComponentName componentName = new ComponentName(context, MainActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
            p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


        }


    }

}
