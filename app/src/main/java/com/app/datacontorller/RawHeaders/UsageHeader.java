package com.app.datacontorller.RawHeaders;

import com.google.gson.annotations.SerializedName;

public class UsageHeader {


    @SerializedName("data_date")
    String date;

    @SerializedName("gsm_usage")
    String gsm_uasge;

    @SerializedName("wifi_usage")
    String wifi_usage;

    @SerializedName("total_usage")
    String total_usage;

    @SerializedName("device_id")
    String device_id;

    @SerializedName("mobileno")
    String mobileno;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGsm_uasge() {
        return gsm_uasge;
    }

    public void setGsm_uasge(String gsm_uasge) {
        this.gsm_uasge = gsm_uasge;
    }

    public String getWifi_usage() {
        return wifi_usage;
    }

    public void setWifi_usage(String wifi_usage) {
        this.wifi_usage = wifi_usage;
    }

    public String getTotal_usage() {
        return total_usage;
    }

    public void setTotal_usage(String total_usage) {
        this.total_usage = total_usage;
    }

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


}
