
package com.app.datacontorller.Models.FileStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("downloadstatus")
    @Expose
    private Downloadstatus downloadstatus;

    public Downloadstatus getDownloadstatus() {
        return downloadstatus;
    }

    public void setDownloadstatus(Downloadstatus downloadstatus) {
        this.downloadstatus = downloadstatus;
    }

}
