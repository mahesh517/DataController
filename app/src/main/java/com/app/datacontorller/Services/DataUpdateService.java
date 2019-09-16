package com.app.datacontorller.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.datacontorller.Models.BackupServer;
import com.app.datacontorller.R;
import com.app.datacontorller.RawHeaders.InstanceHeader;
import com.app.datacontorller.RawHeaders.UsageHeader;
import com.app.datacontorller.Utils.AppUtil;
import com.app.datacontorller.Utils.LoginPrefManager;
import com.app.datacontorller.Utils.PreferenceManager;
import com.app.datacontorller.Utils.SortEnum;
import com.app.datacontorller.WebApi.APIServiceFactory;
import com.app.datacontorller.WebApi.ApiService;
import com.app.datacontorller.data.AppItem;
import com.app.datacontorller.data.DataManager;
import com.app.datacontorller.db.DbHistoryExecutor;
import com.app.datacontorller.db.DbIgnoreExecutor;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("Registered")
public class DataUpdateService extends IntentService {


    ApiService apiService, apiService1, apiService2;

    NotificationChannel channel;
    LoginPrefManager loginPrefManager;
    String device_id;


    List<InstanceHeader> offline_appUsage;
    InstanceHeader offHeader, onHeader;

    private int mDay = 0;

    private long mTotal;

    private PackageManager mPackageManager;

    Long wifi_usage = Long.valueOf(0), gsm_usage = Long.valueOf(0);
    Long Off_wifi_usage = Long.valueOf(0), Off_gsm_usage = Long.valueOf(0);

    public DataUpdateService() {
        super("DataUpdateService");
    }


    private void initBroadCast() {


        BroadcastReceiver status_update_reciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    Log.e("notification", "---");
                }
            }

        };
        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(status_update_reciver, new IntentFilter("updated"));
    }


    private BroadcastReceiver mScreenStateReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case Intent.ACTION_SCREEN_ON:

                    Log.e("screen_lock:", "on");

                    offHeader = new InstanceHeader();
                    Calendar calendar = Calendar.getInstance();

                    loginPrefManager.setOnstartTime(loginPrefManager.getDate(calendar.getTime()));
                    loginPrefManager.setOffendTime(loginPrefManager.getDate(calendar.getTime()));

                    offHeader.setInstance("off");
                    offHeader.setStart_time(loginPrefManager.getOffstartTime());
                    offHeader.setEnd_rime(loginPrefManager.getOffendtTime());
                    offHeader.setDevice_id(device_id);
                    offHeader.setMobileno(loginPrefManager.getStringValue("phone_no"));
                    Log.e("off_header", new Gson().toJson(offHeader));


                    sendInstanceUsage(onHeader);
                    new MyAsyncTask().execute(0, mDay);
                    break;

                case Intent.ACTION_SCREEN_OFF:

                    Log.e("screen_lock:", "off");
                    onHeader = new InstanceHeader();
                    Calendar calendar1 = Calendar.getInstance();

                    loginPrefManager.setOnendTime(loginPrefManager.getDate(calendar1.getTime()));
                    loginPrefManager.setOffstartTime(loginPrefManager.getDate(calendar1.getTime()));

                    onHeader.setDevice_id(device_id);
                    onHeader.setMobileno(loginPrefManager.getStringValue("phone_no"));
                    onHeader.setStart_time(loginPrefManager.getOnstartTime());
                    onHeader.setEnd_rime(loginPrefManager.getOnEndTime());
                    onHeader.setInstance("on");
                    Log.e("onHeader", new Gson().toJson(onHeader));
                    sendInstanceUsage(onHeader);
                    new MyOFFTask().execute(0, mDay);
                    break;
                case Intent.ACTION_USER_PRESENT:

            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        device_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());


        offline_appUsage = new ArrayList<>();

        apiService = APIServiceFactory.getRetrofit().create(ApiService.class);
        apiService1 = APIServiceFactory.getBackupRetrofit().create(ApiService.class);


        PreferenceManager.init(this);
        DataManager.init();
        DbIgnoreExecutor.init(getApplicationContext());
        DbHistoryExecutor.init(getApplicationContext());
        loginPrefManager = new LoginPrefManager(getBaseContext());
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mScreenStateReceiver, filter);


        mPackageManager = getPackageManager();
        initBroadCast();


    }

    @SuppressLint("NewApi")
    private void startMyOwnForeground() {
        String channelName = "Data Monitor Background Service";
        String NOTIFICATION_CHANNEL_ID = "com.thinkingfoxes.datamonitor";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // release receiver

        Log.e("onDestory", "-----");
        unregisterReceiver(mScreenStateReceiver);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        sendBroadcast(new Intent("YouWillNeverKillMe"));


    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Log.e("onTaskRemoved", "-----");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        sendBroadcast(new Intent("YouWillNeverKillMe"));
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotiification(String message) {


        createNotificationChannel();

        String CHANNEL_ID = getString(R.string.default_notification_channel_id);
        Notification newMessageNotification = new Notification.Builder(getBaseContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, newMessageNotification);
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = getString(R.string.default_notification_channel_id);
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    @SuppressLint("StaticFieldLeak")
    class MyAsyncTask extends AsyncTask<Integer, Void, List<AppItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<AppItem> doInBackground(Integer... integers) {
            return DataManager.getInstance().getApps(getBaseContext(), integers[0], integers[1]);
        }

        @Override
        protected void onPostExecute(List<AppItem> appItems) {
            mTotal = 0;
            for (AppItem item : appItems) {


                getAppData(item.mPackageName);
                if (item.mUsageTime <= 0) continue;

                mTotal += item.mUsageTime;

                item.mCanOpen = mPackageManager.getLaunchIntentForPackage(item.mPackageName) != null;
            }
            Log.e("wifi_usage", "--" + AppUtil.humanReadableByteCount(wifi_usage));
            Log.e("gsm_usage", "---" + AppUtil.humanReadableByteCount(gsm_usage));


            Long total_usage = wifi_usage + gsm_usage;
            UsageHeader usageHeader = new UsageHeader();

            Calendar calendar = Calendar.getInstance();

            usageHeader.setDate(loginPrefManager.getDate(calendar.getTime()));
            usageHeader.setDevice_id(device_id);
            usageHeader.setTotal_usage(AppUtil.humanReadableByteCount(total_usage));
            usageHeader.setGsm_uasge(AppUtil.humanReadableByteCount(gsm_usage));
            usageHeader.setWifi_usage(AppUtil.humanReadableByteCount(wifi_usage));
            usageHeader.setMobileno(loginPrefManager.getStringValue("phone_no"));

            sendDailyUsage(usageHeader);
            wifi_usage = Long.valueOf(0);
            gsm_usage = Long.valueOf(0);

        }
    }

    @SuppressLint("StaticFieldLeak")
    class MyOFFTask extends AsyncTask<Integer, Void, List<AppItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<AppItem> doInBackground(Integer... integers) {
            return DataManager.getInstance().getApps(getBaseContext(), integers[0], integers[1]);
        }

        @Override
        protected void onPostExecute(List<AppItem> appItems) {
            mTotal = 0;
            for (AppItem item : appItems) {


                getOffline(item.mPackageName);
                if (item.mUsageTime <= 0) continue;

                mTotal += item.mUsageTime;

                item.mCanOpen = mPackageManager.getLaunchIntentForPackage(item.mPackageName) != null;
            }
            Log.e("wifi_usage", "--" + AppUtil.humanReadableByteCount(Off_wifi_usage));
            Log.e("gsm_usage", "---" + AppUtil.humanReadableByteCount(Off_gsm_usage));


            UsageHeader usageHeader = new UsageHeader();
            Long total_usage = Off_wifi_usage + Off_gsm_usage;


            Calendar calendar = Calendar.getInstance();

            usageHeader.setTotal_usage(AppUtil.humanReadableByteCount(total_usage));
            usageHeader.setDate(loginPrefManager.getDate(calendar.getTime()));
            usageHeader.setDevice_id(device_id);
            usageHeader.setGsm_uasge(AppUtil.humanReadableByteCount(Off_gsm_usage));
            usageHeader.setWifi_usage(AppUtil.humanReadableByteCount(Off_wifi_usage));
            usageHeader.setMobileno(loginPrefManager.getStringValue("phone_no"));

            Off_wifi_usage = Long.valueOf(0);
            Off_gsm_usage = Long.valueOf(0);

            sendDailyUsage(usageHeader);

        }
    }


    private void getAppData(String mPackageName) {
        long totalWifi = 0;
        long totalMobile = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) getSystemService(Context.NETWORK_STATS_SERVICE);
            int targetUid = AppUtil.getAppUid(getPackageManager(), mPackageName);

            long[] range = AppUtil.getTimeRange(SortEnum.getSortEnum(mDay));
            try {
                if (networkStatsManager != null) {
                    NetworkStats networkStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_WIFI, "", range[0], range[1]);
                    if (networkStats != null) {
                        while (networkStats.hasNextBucket()) {
                            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                            networkStats.getNextBucket(bucket);
                            if (bucket.getUid() == targetUid) {
                                totalWifi += bucket.getTxBytes() + bucket.getRxBytes();
                            }
                        }
                    }
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        NetworkStats networkStatsM = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE, tm.getSubscriberId(), range[0], range[1]);
                        if (networkStatsM != null) {
                            while (networkStatsM.hasNextBucket()) {
                                NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                                networkStatsM.getNextBucket(bucket);
                                if (bucket.getUid() == targetUid) {
                                    totalMobile += bucket.getTxBytes() + bucket.getRxBytes();
                                }
                            }
                        }
                    }

                    Long[] long_data = {totalWifi, totalMobile};


                    wifi_usage = wifi_usage + totalWifi;

                    gsm_usage = gsm_usage + totalMobile;


                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void getOffline(String mPackageName) {
        long totalWifi = 0;
        long totalMobile = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) getSystemService(Context.NETWORK_STATS_SERVICE);
            int targetUid = AppUtil.getAppUid(getPackageManager(), mPackageName);

            long[] range = AppUtil.getTimeRange(SortEnum.getSortEnum(mDay));
            try {
                if (networkStatsManager != null) {
                    NetworkStats networkStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_WIFI, "", range[0], range[1]);
                    if (networkStats != null) {
                        while (networkStats.hasNextBucket()) {
                            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                            networkStats.getNextBucket(bucket);
                            if (bucket.getUid() == targetUid) {
                                totalWifi += bucket.getTxBytes() + bucket.getRxBytes();
                            }
                        }
                    }
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        NetworkStats networkStatsM = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE, tm.getSubscriberId(), range[0], range[1]);
                        if (networkStatsM != null) {
                            while (networkStatsM.hasNextBucket()) {
                                NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                                networkStatsM.getNextBucket(bucket);
                                if (bucket.getUid() == targetUid) {
                                    totalMobile += bucket.getTxBytes() + bucket.getRxBytes();
                                }
                            }
                        }
                    }

                    Long[] long_data = {totalWifi, totalMobile};


                    Off_wifi_usage = wifi_usage + totalWifi;

                    Off_gsm_usage = gsm_usage + totalMobile;


                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    private void sendStatusApi(String message) {

        apiService1.sendstatus(message).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                Log.e("response", response.raw().toString());

                Log.e("DiscoverApiReq", new Gson().toJson(response.raw().request()));
                Log.e("DiscoverApiResp", new Gson().toJson(response.body()));


                Log.e("response", "--" + response.raw().code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });


    }


    private void sendInstanceUsage(InstanceHeader instanceHeader) {
        try {

            apiService.instanceusage(instanceHeader).enqueue(new Callback<BackupServer>() {
                @Override
                public void onResponse(Call<BackupServer> call, Response<BackupServer> response) {


                    try {
                        sendStatusApi("Instance api status for " + loginPrefManager.getStringValue("phone_no") + response.body().getStatus());
                    } catch (Exception e) {

                        offline_appUsage.add(instanceHeader);
                        sendStatusApi("Exception in instance api " + loginPrefManager.getStringValue("phone_no") + e.getMessage());
                    }

                    if (response.body().getStatus().equalsIgnoreCase("error")){
                        offline_appUsage.add(instanceHeader);
                    }

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        if (offline_appUsage != null && offline_appUsage.size() > 0) {

                            Log.e("offline_appUsage", "--" + offline_appUsage.size());
                            for (int i = 0; i < offline_appUsage.size(); i++) {
                                sendOfflineData(i, offline_appUsage.get(i));
                                break;
                            }
                        }

                    }

                }

                @Override
                public void onFailure(Call<BackupServer> call, Throwable t) {

                    try {
                        offline_appUsage.add(instanceHeader);
                        sendStatusApi("Instance api failure due to " + loginPrefManager.getStringValue("phone_no") + t.getMessage());
                    } catch (Exception e) {

                        offline_appUsage.add(instanceHeader);
                        sendStatusApi("Instance api failure exception due to " + loginPrefManager.getStringValue("phone_no") + e.getMessage());
                    }

                }
            });
        } catch (Exception e) {
            try {
                offline_appUsage.add(instanceHeader);
                sendStatusApi("Instance api try/catch due to " + loginPrefManager.getStringValue("phone_no") + e.getMessage());
            } catch (Exception e1) {

                offline_appUsage.add(instanceHeader);
                sendStatusApi("Instance api failure exception due to " + loginPrefManager.getStringValue("phone_no") + e1.getMessage());
            }
        }
    }

    private void sendDailyUsage(UsageHeader usageHeader) {
        try {

            apiService.senddailyUsage(usageHeader).enqueue(new Callback<BackupServer>() {
                @Override
                public void onResponse(Call<BackupServer> call, Response<BackupServer> response) {


                    try {
                        sendStatusApi("Daily Usage api status for " + loginPrefManager.getStringValue("phone_no") + response.body().getStatus());
                    } catch (Exception e) {
                        sendStatusApi("Exception in Daily Usage api " + loginPrefManager.getStringValue("phone_no") + e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<BackupServer> call, Throwable t) {

                    try {
                        sendStatusApi("Daily Usage ap failure due to " + loginPrefManager.getStringValue("phone_no") + t.getMessage());
                    } catch (Exception e) {
                        sendStatusApi("Daily Usage ap failure exception due to " + loginPrefManager.getStringValue("phone_no") + e.getMessage());
                    }

                }
            });
        } catch (Exception e) {
            try {
                sendStatusApi("Daily Usage api try/catch due to " + loginPrefManager.getStringValue("phone_no") + e.getMessage());
            } catch (Exception e1) {
                sendStatusApi("Daily Usage api failure exception due to " + loginPrefManager.getStringValue("phone_no") + e1.getMessage());
            }
        }
    }


    private void sendOfflineData(int poistion, InstanceHeader appData) {
        try {


            Log.e("offline_usage", "--" + poistion);

            apiService.instanceusage(appData).enqueue(new Callback<BackupServer>() {
                @Override
                public void onResponse(Call<BackupServer> call, Response<BackupServer> response) {
                    Log.e("onStatus", new Gson().toJson(response.body().getStatus()));


                    try {
                        sendStatusApi("Saved instance on  '" + appData.getStart_time() + "'" + loginPrefManager.getStringValue("phone_no") + " " + response.body().getStatus());

                        if (offline_appUsage != null && offline_appUsage.size() > 0) {
                            offline_appUsage.remove(poistion);
                            for (int i = 0; i < offline_appUsage.size(); i++) {
                                sendOfflineData(i, offline_appUsage.get(i));
                                break;
                            }
                        }
                    } catch (Exception e) {
                        sendStatusApi("Exception occured Saved instance on  '" + appData.getStart_time() + "'" + loginPrefManager.getStringValue("phone_no"));

                    }

                }

                @Override
                public void onFailure(Call<BackupServer> call, Throwable t) {

//                    Log.e("onFailure", t.getMessage());

                    try {
                        sendStatusApi("Saved Instance failure for " + loginPrefManager.getStringValue("phone_no") + " " + t.getMessage());

                    } catch (Exception e) {
                        sendStatusApi("Saved Instance failure Exception for " + loginPrefManager.getStringValue("phone_no") + " " + e.getMessage());

                    }


                }
            });


        } catch (Exception e) {


            sendStatusApi("Saved Instance Exception  for " + loginPrefManager.getStringValue("phone_no") + " " + e.getMessage());

            sendBroadcast(new Intent("YouWillNeverKillMe"));
            Log.e("Exception", e.getMessage());
        }
    }


}
