package com.app.datacontorller.RawHeaders;

import com.google.gson.annotations.SerializedName;

public class DataUsageheader {

    @SerializedName("device_id")
    private String device_id;

    @SerializedName("mobileno")
    private String mobileno;

    @SerializedName("total_usage")
    private String total_usage;

    @SerializedName("datadate")
    private String datadate;

    @SerializedName("gsm_usage")
    private String gsm_usage;

    @SerializedName("wifi_usage")
    private String wifi_usage;

    public String getWifi_usage() {
        return wifi_usage;
    }

    public void setWifi_usage(String wifi_usage) {
        this.wifi_usage = wifi_usage;
    }

    public String getGsm_usage() {
        return gsm_usage;
    }

    public void setGsm_usage(String gsm_usage) {
        this.gsm_usage = gsm_usage;
    }

    public String getDatadate() {
        return datadate;
    }

    public void setDatadate(String datadate) {
        this.datadate = datadate;
    }

    public String getTotal_usage() {
        return total_usage;
    }

    public void setTotal_usage(String total_usage) {
        this.total_usage = total_usage;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
