
package com.app.datacontorller.Models.UpdateMobileNo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Updated {

    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("error")
    @Expose
    private String error;

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
