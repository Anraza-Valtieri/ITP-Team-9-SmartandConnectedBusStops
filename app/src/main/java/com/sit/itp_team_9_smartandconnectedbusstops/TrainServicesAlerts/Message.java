
package com.sit.itp_team_9_smartandconnectedbusstops.TrainServicesAlerts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("Content")
    @Expose
    private String content;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}
