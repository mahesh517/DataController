
package com.app.datacontorller.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Application {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("application_name")
    @Expose
    private String applicationName;
    @SerializedName("wifi_total_usage")
    @Expose
    private Integer wifiTotalUsage;
    @SerializedName("gsm_total_usage")
    @Expose
    private Integer gsmTotalUsage;
    @SerializedName("wifi_instance_usage")
    @Expose
    private Integer wifiInstanceUsage;
    @SerializedName("gsm_instance_usage")
    @Expose
    private Integer gsmInstanceUsage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getWifiTotalUsage() {
        return wifiTotalUsage;
    }

    public void setWifiTotalUsage(Integer wifiTotalUsage) {
        this.wifiTotalUsage = wifiTotalUsage;
    }

    public Integer getGsmTotalUsage() {
        return gsmTotalUsage;
    }

    public void setGsmTotalUsage(Integer gsmTotalUsage) {
        this.gsmTotalUsage = gsmTotalUsage;
    }

    public Integer getWifiInstanceUsage() {
        return wifiInstanceUsage;
    }

    public void setWifiInstanceUsage(Integer wifiInstanceUsage) {
        this.wifiInstanceUsage = wifiInstanceUsage;
    }

    public Integer getGsmInstanceUsage() {
        return gsmInstanceUsage;
    }

    public void setGsmInstanceUsage(Integer gsmInstanceUsage) {
        this.gsmInstanceUsage = gsmInstanceUsage;
    }

}
