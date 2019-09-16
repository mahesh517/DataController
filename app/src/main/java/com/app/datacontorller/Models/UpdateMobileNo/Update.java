
package com.app.datacontorller.Models.UpdateMobileNo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Update {

    @SerializedName("updated")
    @Expose
    private List<Updated> updated = null;

    public List<Updated> getUpdated() {
        return updated;
    }

    public void setUpdated(List<Updated> updated) {
        this.updated = updated;
    }

}
