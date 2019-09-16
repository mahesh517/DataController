
package com.app.datacontorller.Models.TimeApp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datamonitor {

    @SerializedName("time_span")
    @Expose
    private List<TimeSpan> timeSpan = null;
    @SerializedName("application_name")
    @Expose
    private String applicationName;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("total_usage")
    @Expose
    private String totalUsage;

    public List<TimeSpan> getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(List<TimeSpan> timeSpan) {
        this.timeSpan = timeSpan;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(String totalUsage) {
        this.totalUsage = totalUsage;
    }

}
