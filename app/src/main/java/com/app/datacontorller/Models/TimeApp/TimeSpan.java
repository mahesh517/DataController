
package com.app.datacontorller.Models.TimeApp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSpan {

    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("usage_time")
    @Expose
    private String usageTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(String usageTime) {
        this.usageTime = usageTime;
    }

}
