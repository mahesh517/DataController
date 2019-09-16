
package com.app.datacontorller.Models.TimeApp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("datamonitor")
    @Expose
    private List<Datamonitor> datamonitor = null;

    public List<Datamonitor> getDatamonitor() {
        return datamonitor;
    }

    public void setDatamonitor(List<Datamonitor> datamonitor) {
        this.datamonitor = datamonitor;
    }

}
