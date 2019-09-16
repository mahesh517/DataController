package com.app.datacontorller.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.app.datacontorller.MainActivity;
import com.app.datacontorller.R;
import com.app.datacontorller.Services.DataUpdateService;
import com.app.datacontorller.Utils.LoginPrefManager;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {


    LoginPrefManager loginPrefManager;

    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);


        loginPrefManager = new LoginPrefManager(SplashActivity.this);
        Log.e("service status:", "--" + isMyServiceRunning(DataUpdateService.class));

        Calendar calendar = Calendar.getInstance();


        loginPrefManager.setOnstartTime(loginPrefManager.getDate(calendar.getTime()));
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {


                if (!hasPermissionToReadNetworkHistory()) {
                    startActivity(new Intent(SplashActivity.this, PermissionActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        };

        handler.postDelayed(runnable, 2000);
//        loginPrefManager.setOnstartTime(loginPrefManager.getDate(calendar.getTime()));

//        Log.e("onStartTime", loginPrefManager.getOnstartTime());


        Calendar cal = Calendar.getInstance();


        cal.set(2019, 8, 10, 0, 0, 00);
        calendar.set(2019, 8, 10, 23, 59, 59);


        Log.e("start_time", "--" + cal.getTime());
        Log.e("end_time", "--" + calendar.getTime());
//        if (!isMyServiceRunning(DataUpdateService.class)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
////                new SingleApp().execute(0, 0);
//                startForegroundService(new Intent(this, DataUpdateService.class));
//            } else {
//                startService(new Intent(this, DataUpdateService.class));
//            }
//        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
//                Log.e("isMyServiceRunning?", true + "");
                return true;
            }
        }
        return false;
    }

    private boolean hasPermissionToReadNetworkHistory() {

        final AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {

            Log.e("allowed", "success");
            return true;
        }

        return false;
    }
}
