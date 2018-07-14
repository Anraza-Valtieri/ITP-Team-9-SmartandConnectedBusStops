
package com.sit.itp_team_9_smartandconnectedbusstops.TrainServicesAlerts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Value {

    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("AffectedSegments")
    @Expose
    private List<AffectedSegment> affectedSegments = null;
    @SerializedName("Message")
    @Expose
    private List<Message> message = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<AffectedSegment> getAffectedSegments() {
        return affectedSegments;
    }

    public void setAffectedSegments(List<AffectedSegment> affectedSegments) {
        this.affectedSegments = affectedSegments;
    }

    public List<Message> getMessage() {
        return message;
    }

    public void setMessage(List<Message> message) {
        this.message = message;
    }

}
