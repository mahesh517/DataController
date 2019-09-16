package com.app.datacontorller.RawHeaders;

import com.google.gson.annotations.SerializedName;

public class InstanceHeader {


    @SerializedName("mobileno")
    private String device_id;

    @SerializedName("device_id")
    private String mobileno;

    @SerializedName("instance")
    private String instance;

    @SerializedName("start_time")
    private String start_time;

    @SerializedName("end_time")
    private String end_rime;


    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }


    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_rime() {
        return end_rime;
    }

    public void setEnd_rime(String end_rime) {
        this.end_rime = end_rime;
    }
}
