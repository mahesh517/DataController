package com.app.datacontorller.BroadCastRecivers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.app.datacontorller.Services.DataUpdateService;

public class RestartReceiver extends BroadcastReceiver {

    private static final String TAG = "RestartServiceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive");


        if (!isMyServiceRunning(DataUpdateService.class, context)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                context.startForegroundService(new Intent(context, DataUpdateService.class));
            } else {
                context.startService(new Intent(context, DataUpdateService.class));
            }
        } else {
            Log.e("service running", "--" + isMyServiceRunning(DataUpdateService.class, context));
        }


//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                context.startForegroundService(new Intent(context, DataUpdateService.class));
//            } else {
//                context.startService(new Intent(context, DataUpdateService.class));
//            }
//        } catch (Exception e) {
//            Log.e("restert", e.getMessage());
//        }

    }


    private boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
//                Log.e("isMyServiceRunning?", true + "");
                return true;
            }
        }
//        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

}
