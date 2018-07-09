
package com.sit.itp_team_9_smartandconnectedbusstops.SGUV;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Index {

    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
