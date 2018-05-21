package com.sit.itp_team_9_smartandconnectedbusstops;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LTABusStopData {

    /*
    List of Results of Busstops
     */
    @SerializedName("value")
    public List<LTABusStopResult> results;

    @SerializedName("BusStopCode")
    public String BusStopCode;
    @SerializedName("RoadName")
    public String RoadName;
    @SerializedName("Description")
    public String Description;

    public String query;

    public List<LTABusStopResult> getResults() {
        return results;
    }

    public void setResults(List<LTABusStopResult> results) {
        this.results = results;
    }

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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
