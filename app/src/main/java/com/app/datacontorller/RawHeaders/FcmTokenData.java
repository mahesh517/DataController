package com.app.datacontorller.RawHeaders;

import com.google.gson.annotations.SerializedName;

public class FcmTokenData {

    @SerializedName("device_id")
    private String device_id;

    @SerializedName("device_token")
    private String device_token;

    @SerializedName("platform")
    private String platform;

    @SerializedName("mobile_no")
    private String mobileno;

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @SerializedName("os_version")
    private String os_version;

    @SerializedName("model")
    private String model;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
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
