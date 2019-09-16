
package com.app.datacontorller.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("datamonitor")
    @Expose
    private Datamonitor datamonitor;

    public Datamonitor getDatamonitor() {
        return datamonitor;
    }

    public void setDatamonitor(Datamonitor datamonitor) {
        this.datamonitor = datamonitor;
    }

}
