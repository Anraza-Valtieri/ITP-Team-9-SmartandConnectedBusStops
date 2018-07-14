
package com.sit.itp_team_9_smartandconnectedbusstops.TrainServicesAlerts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainServicesAlerts {

    @SerializedName("odata.metadata")
    @Expose
    private String odataMetadata;
    @SerializedName("value")
    @Expose
    private Value value;

    public String getOdataMetadata() {
        return odataMetadata;
    }

    public void setOdataMetadata(String odataMetadata) {
        this.odataMetadata = odataMetadata;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }


}
