package com.app.datacontorller.data;

import com.google.gson.annotations.SerializedName;

public class AppTime {

    @SerializedName("start_time")
    private String start_time;

    @SerializedName("end_time")
    private String end_time;


    @SerializedName("duration")
    private String usage_time;

    @SerializedName("total_usage")
    private String usage;


    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getUsage_time() {
        return usage_time;
    }

    public void setUsage_time(String usage_time) {
        this.usage_time = usage_time;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
