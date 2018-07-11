
package com.sit.itp_team_9_smartandconnectedbusstops.SGUV;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("update_timestamp")
    @Expose
    private String updateTimestamp;
    @SerializedName("index")
    @Expose
    private List<Index> index = null;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public List<Index> getIndex() {
        return index;
    }

    public void setIndex(List<Index> index) {
        this.index = index;
    }

}
