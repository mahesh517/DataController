package com.app.datacontorller.RawHeaders;

import com.google.gson.annotations.SerializedName;

public class UpdateMobileNoHeader {

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    @SerializedName("mobileno")

    String mobileno;
}
