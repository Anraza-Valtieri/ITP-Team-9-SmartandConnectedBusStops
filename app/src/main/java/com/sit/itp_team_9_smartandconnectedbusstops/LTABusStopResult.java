package com.sit.itp_team_9_smartandconnectedbusstops;

import com.google.gson.annotations.SerializedName;

public class LTABusStopResult {

    @SerializedName("BusStopCode")
    public String BusStopCode;
    @SerializedName("RoadName")
    public String RoadName;
    @SerializedName("Description")
    public String Description;

    public String getBusStopCode() {
        return BusStopCode;
    }

    public void setBusStopCode(String busStopCode) {
        BusStopCode = busStopCode;
    }

    public String getRoadName() {
        return RoadName;
    }

    public void setRoadName(String roadName) {
        RoadName = roadName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
