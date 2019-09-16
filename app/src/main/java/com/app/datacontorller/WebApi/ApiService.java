package com.app.datacontorller.WebApi;


import com.app.datacontorller.Models.BackupServer;
import com.app.datacontorller.Models.UpdateMobileNo.UpdateMobileNo;
import com.app.datacontorller.RawHeaders.FcmTokenData;
import com.app.datacontorller.RawHeaders.InstanceHeader;
import com.app.datacontorller.RawHeaders.UpdateMobileNoHeader;
import com.app.datacontorller.RawHeaders.UsageHeader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {


    @POST("datadownload/updateMobile/{deviceID}")
    Call<UpdateMobileNo> updatemobileNo(@Body UpdateMobileNoHeader updateMobileNoHeader, @Path("deviceID") String _id);

    @FormUrlEncoded
    @POST("test3.php?123")
    Call<ResponseBody> sendstatus(@Field("value") String value);

    @POST("device_token")
    Call<BackupServer> updateDevicetoken(@Body FcmTokenData fcmTokenData);

    @POST("instance_usage")
    Call<BackupServer> instanceusage(@Body InstanceHeader instanceHeader);

    @POST("daily_usage")
    Call<BackupServer> senddailyUsage(@Body UsageHeader usageHeader);


}
